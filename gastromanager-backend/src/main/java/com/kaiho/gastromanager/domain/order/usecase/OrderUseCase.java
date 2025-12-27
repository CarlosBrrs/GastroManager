package com.kaiho.gastromanager.domain.order.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.exception.InsufficientStockException;
import com.kaiho.gastromanager.domain.order.exception.NotPermittedOrderStatusChangeException;
import com.kaiho.gastromanager.domain.order.exception.OrderDoesNotExistException;
import com.kaiho.gastromanager.domain.order.model.ChangeOrderStatus;
import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.product.api.ProductServicePort;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductIngredient;
import com.kaiho.gastromanager.domain.product.model.ProductMode;
import com.kaiho.gastromanager.domain.product.model.ProductRecipe;
import com.kaiho.gastromanager.domain.recipe.model.Recipe;
import com.kaiho.gastromanager.domain.recipeingredient.model.RecipeIngredient;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.config.generator.OrderCodeGenerator;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.kaiho.gastromanager.domain.order.model.OperationalStatus.PENDING;
import static com.kaiho.gastromanager.domain.order.model.OperationalStatus.PREPARING;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class OrderUseCase implements OrderServicePort {

    private final OrderPersistencePort orderPersistencePort;
    private final IngredientServicePort ingredientServicePort;
    private final ProductServicePort productServicePort;
    private final RestaurantServicePort restaurantServicePort;

    @Override
    public Page<Order> getAllOrders(OrderSearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.sortDirection().equalsIgnoreCase("desc") ? DESC : ASC,
                criteria.sortBy()
        );
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
        return orderPersistencePort.findAllOrders(criteria, pageable);
    }

    @Override
    @Transactional
    public UUID createOrder(Order order) {
        changeOperationalStatus(order);
        List<UUID> requestedProductUuids = order.getOrderItems().stream()
                                                .map(orderItem -> orderItem.getProduct().getUuid())
                                                .toList();
        List<Product> foundProducts = productServicePort.findAllByUuidInAndRestaurant(requestedProductUuids, getCurrentRestaurant());
        validateProductsExist(foundProducts, requestedProductUuids);

        // Crear mapa de productos para acceso rápido
        Map<UUID, Product> productMap = foundProducts.stream()
                                                     .collect(toMap(Product::getUuid, Function.identity()));

        // Calcular las cantidades requeridas de ingredientes (una sola vez)
        Map<UUID, Double> requiredIngredientQuantities = calculateRequiredIngredients(order.getOrderItems(), productMap);

        // Validar que hay stock suficiente de ingredientes antes de crear la orden
        validateIngredientsStock(requiredIngredientQuantities);

        // Generar código antes de descontar stock (para incluirlo en el registro)
        String orderCode = generateFiveLengthUniqueCode();
        order.setCode(orderCode);

        // Descontar el stock de los ingredientes
        decreaseIngredientsStockOrder(requiredIngredientQuantities, orderCode);

        calculateAndAssignOrderItemPrices(order.getOrderItems(), productMap);

        calculateAndAssignTotalAmount(order);

        Order savedOrder = orderPersistencePort.createOrder(order);

        return savedOrder.getUuid();

    }

    private void changeOperationalStatus(Order order) {
        Restaurant restaurant = restaurantServicePort.getRestaurantById(order.getRestaurant().getUuid());
        if (restaurant.getConfig().isPayBeforeOrder()) {
            // Si requiere pago previo, inicia esperando pago
            order.setOperationalStatus(OperationalStatus.AWAITING_PAYMENT);
            order.setRequiresPaymentBefore(true);
        } else {
            // Si no requiere pago previo, va directo a cocina
            order.setOperationalStatus(OperationalStatus.PENDING);
            order.setRequiresPaymentBefore(false);
        }
    }

    private void calculateAndAssignTotalAmount(Order order) {
        BigDecimal totalAmount = order.getOrderItems().stream()
                                      .map(OrderItem::getSubtotal)
                                      .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
    }

    private void calculateAndAssignOrderItemPrices(List<OrderItem> orderItems, Map<UUID, Product> productMap) {
        for (OrderItem orderItem : orderItems) {
            UUID productUuid = orderItem.getProduct().getUuid();
            Product product = productMap.get(productUuid);

            BigDecimal unitPrice = product.getSalePrice();
            orderItem.setUnitPrice(unitPrice);

            BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
            BigDecimal subtotal = unitPrice.multiply(quantity);
            orderItem.setSubtotal(subtotal);

            orderItem.setProduct(product);
        }
    }

    private void validateProductsExist(List<Product> products, List<UUID> requestedProductUuids) {
        List<UUID> foundProductUuids = products.stream()
                                               .map(Product::getUuid)
                                               .toList();

        List<UUID> missingProducts = requestedProductUuids.stream()
                                                          .filter(uuid -> !foundProductUuids.contains(uuid))
                                                          .toList();

        if (!missingProducts.isEmpty()) {
            throw new IllegalArgumentException("Los siguientes productos no existen: " + missingProducts);
        }
    }

    @Override
    public UUID changeInvoicingStatus(UUID orderUuid, String newStatus) {
        return null;
    }

    //    @Override
    public UUID changeInvoicingStatus(UUID orderUuid, String newStatus, String reason, UUID userUuid) {
        Order order = orderPersistencePort.getOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));

//        order.setInvoicingStatus(newStatus);
        /*// TODO: Change to database impl orderstatus
        if (!isTransitionAllowed(order.getStatus().name(), newStatus, user.roles())) {
            throw new UnauthorizedOrderStatusChangeException(userUuid, newStatus);
        }
*/
        return orderPersistencePort.changeOrderStatus(orderUuid, newStatus, reason);

    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByUUID(UUID orderUuid, UUID restaurantUuid) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, restaurantUuid)
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));

        // Calcular el restante a pagar
        calculateRemainingToPay(order);

        return order;
    }

    private void calculateRemainingToPay(Order order) {
        BigDecimal totalPaid = order.getTotalPaid() != null ? order.getTotalPaid() : BigDecimal.ZERO;
        BigDecimal remainingToPay = order.getTotalAmount().subtract(totalPaid);
        order.setRemainingToPay(remainingToPay);
    }

    //TODO: Modify the list and return void
/*    private List<OrderItem> addPricesToOrderItems(List<OrderItem> orderItems, Map<UUID, Double> productItemPriceMap) {
        List<OrderItem> orderItemsWithPrices = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Double unitPrice = productItemPriceMap.get(orderItem.getProductItem().getUuid());
            if (unitPrice == null) {
                throw new IllegalArgumentException("El producto con UUID " + orderItem.getProductItem().getUuid() + " no se encuentra disponible.");
            }
//            orderItem.setSellingPrice(unitPrice);
            orderItemsWithPrices.add(orderItem);
        }
        return orderItemsWithPrices;
    }*/

    /**
     * Valida que hay stock suficiente de ingredientes para procesar todos los OrderItems.
     * <p>
     * Este método maneja tres escenarios:
     * 1. Productos en modo BASIC: No requieren validación de stock (no tienen ingredientes)
     * 2. Productos en modo ADVANCED con ingredientes directos: Valida ingredientes del producto
     * 3. Productos en modo ADVANCED con recetas: Valida ingredientes de cada receta
     *
     * @param requiredIngredientQuantities Mapa con UUID del ingrediente -> cantidad requerida
     * @throws InsufficientStockException Si no hay stock suficiente de algún ingrediente
     */
    private void validateIngredientsStock(Map<UUID, Double> requiredIngredientQuantities) {
        // Si no hay ingredientes requeridos (todos los productos son BASIC), no validar nada
        if (requiredIngredientQuantities.isEmpty()) {
            return;
        }

        // Obtener todos los ingredientes necesarios de la base de datos en una sola consulta
        List<Ingredient> ingredients = ingredientServicePort.getIngredientsByUuid(requiredIngredientQuantities.keySet());

        // Validar el stock disponible de cada ingrediente
        for (Map.Entry<UUID, Double> entry : requiredIngredientQuantities.entrySet()) {
            UUID ingredientUuid = entry.getKey();
            double requiredQuantity = entry.getValue();

            // Buscar el ingrediente en la lista obtenida
            Ingredient ingredient = ingredients.stream()
                                               .filter(ing -> ing.getUuid().equals(ingredientUuid))
                                               .findFirst()
                                               .orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));

            // Validar que hay stock suficiente
            if (ingredient.getAvailableStock() < requiredQuantity) {
                throw new InsufficientStockException(
                        ingredient.getName(),
                        requiredQuantity,
                        ingredient.getAvailableStock()
                );
            }
        }
    }

    /**
     * Calcula la cantidad total requerida de cada ingrediente para todos los OrderItems.
     * <p>
     * Este método procesa cada producto según su modo:
     * - BASIC: No calcula nada (no tiene ingredientes)
     * - ADVANCED: Suma ingredientes directos + ingredientes de recetas
     *
     * @param orderItems Los items de la orden
     * @param productMap Mapa de productos por UUID
     * @return Mapa con UUID del ingrediente -> cantidad total requerida
     */
    private Map<UUID, Double> calculateRequiredIngredients(List<OrderItem> orderItems, Map<UUID, Product> productMap) {
        Map<UUID, Double> requiredQuantities = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            Product product = productMap.get(orderItem.getProduct().getUuid());

            // Si el producto es BASIC, no tiene ingredientes que validar
            if (product.getMode() == ProductMode.BASIC) {
                continue;
            }

            // Procesar ingredientes directos del producto (si existen)
            if (product.getIngredients() != null && !product.getIngredients().isEmpty()) {
                processProductIngredients(product.getIngredients(), orderItem.getQuantity(), requiredQuantities);
            }

            // Procesar ingredientes de las recetas asociadas (si existen)
            if (product.getRecipes() != null && !product.getRecipes().isEmpty()) {
                processProductRecipes(product.getRecipes(), orderItem.getQuantity(), requiredQuantities);
            }
        }

        return requiredQuantities;
    }

    /**
     * Procesa los ingredientes directos de un producto y acumula las cantidades necesarias.
     *
     * @param productIngredients Lista de ingredientes directos del producto
     * @param orderQuantity      Cantidad del producto en la orden
     * @param requiredQuantities Mapa acumulador de cantidades requeridas
     */
    private void processProductIngredients(
            List<ProductIngredient> productIngredients,
            int orderQuantity,
            Map<UUID, Double> requiredQuantities
    ) {
        for (ProductIngredient productIngredient : productIngredients) {
            UUID ingredientUuid = productIngredient.getIngredient().getUuid();

            // Cantidad necesaria = cantidad del ingrediente en el producto * cantidad de productos en la orden
            double neededQuantity = productIngredient.getQuantity() * orderQuantity;

            // Acumular la cantidad en el mapa (si ya existe, sumar)
            requiredQuantities.merge(ingredientUuid, neededQuantity, Double::sum);
        }
    }

    /**
     * Procesa las recetas de un producto y acumula las cantidades de ingredientes necesarias.
     * <p>
     * Cada receta puede tener un multiplicador de cantidad (quantityMultiplier) que indica
     * cuántas porciones de la receta se usan en el producto.
     *
     * @param productRecipes     Lista de recetas del producto
     * @param orderQuantity      Cantidad del producto en la orden
     * @param requiredQuantities Mapa acumulador de cantidades requeridas
     */
    private void processProductRecipes(
            List<ProductRecipe> productRecipes,
            int orderQuantity,
            Map<UUID, Double> requiredQuantities
    ) {
        for (ProductRecipe productRecipe : productRecipes) {
            Recipe recipe = productRecipe.getRecipe();
            double quantityMultiplier = productRecipe.getQuantityMultiplier();

            // Si la receta no tiene ingredientes, continuar con la siguiente
            if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
                continue;
            }

            // Procesar cada ingrediente de la receta
            for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
                UUID ingredientUuid = recipeIngredient.getIngredient().getUuid();

                // Cantidad necesaria = cantidad en receta * multiplicador de receta * cantidad de productos en la orden
                double neededQuantity = recipeIngredient.getQuantity() * quantityMultiplier * orderQuantity;

                // Acumular la cantidad en el mapa (si ya existe, sumar)
                requiredQuantities.merge(ingredientUuid, neededQuantity, Double::sum);
            }
        }
    }

    private String generateFiveLengthUniqueCode() {
        String base36;
        do {
            base36 = OrderCodeGenerator.getBase36(5);
        } while (orderPersistencePort.existsOrderByOrderCode(base36));
        return base36;
    }

    /**
     * Descuenta el stock de ingredientes para una orden.
     * <p>
     * Este método utiliza el mapa de cantidades requeridas calculado previamente
     * y delega al servicio de ingredientes para realizar el ajuste en batch.
     * <p>
     * Si el mapa está vacío (productos en modo BASIC sin ingredientes), no hace nada.
     * <p>
     * NOTA: El método batchAdjustStock espera cantidades POSITIVAS para descontar,
     * ya que internamente hace: newStock = availableStock - adjustment
     *
     * @param requiredIngredientQuantities Mapa con UUID del ingrediente -> cantidad a descontar
     * @param orderCode                    Código de la orden (para registro/auditoría del ajuste)
     */
    private void decreaseIngredientsStockOrder(Map<UUID, Double> requiredIngredientQuantities, String orderCode) {
        // Si no hay ingredientes que descontar (todos los productos son BASIC), terminar
        if (requiredIngredientQuantities.isEmpty()) {
            return;
        }

        // Realizar el ajuste en batch de todos los ingredientes
        // Las cantidades ya son positivas, el método batchAdjustStock se encarga de restar
        String adjustmentReason = "Descuento por orden #" + orderCode;
        ingredientServicePort.batchAdjustStock(requiredIngredientQuantities, adjustmentReason);
    }

    @Override
    public BigDecimal[] getOrderTotals(UUID orderUuid) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));
        BigDecimal totalAmount = order.getTotalAmount();
        BigDecimal totalPaid = order.getTotalPaid();
        return new BigDecimal[]{totalAmount, totalPaid};
    }

    @Override
    public UUID changeOrderStatus(UUID orderUuid, ChangeOrderStatus orderStatus) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));
        if (orderStatus.getNewStatus() == PREPARING && order.getOperationalStatus() != PENDING) {
            throw new NotPermittedOrderStatusChangeException(orderUuid, order.getOperationalStatus(), orderStatus.getNewStatus());
        }
        order.setOperationalStatus(orderStatus.getNewStatus());
        // todo reason is not being used but it will be for audit purposes in a new table
//        String reason = orderStatus.getReason();
        orderPersistencePort.updateOrder(order);
        return order.getUuid();
    }

    @Override
    public void updateOrderTotalPaid(UUID orderUuid, BigDecimal nuevoTotalPaid) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));
        order.setTotalPaid(nuevoTotalPaid);
        orderPersistencePort.updateOrder(order);
    }

    @Override
    public void updateOrderTotalPaidAndStatus(UUID orderUuid, BigDecimal nuevoTotalPaid, OperationalStatus operationalStatus) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));
        order.setTotalPaid(nuevoTotalPaid);
        order.setOperationalStatus(operationalStatus);
        orderPersistencePort.updateOrder(order);
    }

    @Override
    public void updateOrderPaymentStatus(UUID orderUuid, PaymentStatus paymentStatus) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));
        order.setPaymentStatus(paymentStatus);
        orderPersistencePort.updateOrder(order);
    }

    @Override
    public void updateOrderOperationalStatus(UUID orderUuid, OperationalStatus operationalStatus) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));
        order.setOperationalStatus(operationalStatus);
        orderPersistencePort.updateOrder(order);
    }

    @Override
    public void updateOrderOperationalStatusToPendingIfAwaiting(UUID orderUuid) {
        Order order = orderPersistencePort.findOrderByUuid(orderUuid, getCurrentRestaurant())
                                          .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));

        // Solo cambiar a PENDING si actualmente está en AWAITING_PAYMENT
        if (order.getOperationalStatus() == OperationalStatus.AWAITING_PAYMENT) {
            order.setOperationalStatus(OperationalStatus.PENDING);
            orderPersistencePort.updateOrder(order);
        }
        // Si no está en AWAITING_PAYMENT, no hacer nada (para restaurantes sin pago previo)
    }

}
