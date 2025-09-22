package com.kaiho.gastromanager.domain.order.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.exception.InsufficientStockException;
import com.kaiho.gastromanager.domain.order.exception.OrderDoesNotExistException;
import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.product.api.ProductServicePort;
import com.kaiho.gastromanager.domain.product.model.Product;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.kaiho.gastromanager.domain.order.model.OperationalStatus.PENDING;
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

        Map<UUID, Product> productMap = foundProducts.stream()
                                                     .collect(toMap(Product::getUuid, Function.identity()));

        calculateAndAssignOrderItemPrices(order.getOrderItems(), productMap);

        calculateAndAssignTotalAmount(order);

        order.setCode(generateFiveLengthUniqueCode());

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

    private void validateIngredientsStock(List<OrderItem> orderItems) {

        // Obtener cantidades necesarias por ingrediente en la orden
        Map<UUID, Double> requiredIngredientQuantities = calculateRequiredIngredients(orderItems);

        // Consultar todos los ingredientes necesarios de una vez
        List<Ingredient> ingredients = ingredientServicePort.getIngredientsByUuid(requiredIngredientQuantities.keySet());

        // Para cada registro del mapa de ingredientes
        for (Map.Entry<UUID, Double> entry : requiredIngredientQuantities.entrySet()) {
            UUID ingredientUuid = entry.getKey();
            double requiredQuantity = entry.getValue();

            Ingredient ingredient = ingredients.stream().filter(ing -> ing.getUuid().equals(ingredientUuid)).findFirst().orElseThrow(() -> new IngredientDoesNotExistException(ingredientUuid.toString()));
            // Si tengo menos stock del que quiero usar para la orden lanzar excepcion
            if (ingredient.getAvailableStock() < requiredQuantity) {
                throw new InsufficientStockException(ingredient.getName(), requiredQuantity, ingredient.getAvailableStock());
            }
        }
    }

    private Map<UUID, Double> calculateRequiredIngredients(List<OrderItem> orderItems) {
        return null;

/*        List<ProductItemIngredient> productItemIngredients = orderItems.stream()
                                                                       .flatMap(orderItem -> orderItem.getProductItem().getIngredients().stream()
                                                                                                      .peek(productItemIngredient -> productItemIngredient.setProductItem(orderItem.getProductItem()))
                                                                       ).toList();

        Map<UUID, Double> requiredQuantities = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            List<ProductItemIngredient> relatedIngredients = productItemIngredients.stream()
                                                                                   .filter(ingredient -> ingredient.getProductItem().getUuid().equals(orderItem.getProductItem().getUuid()))
                                                                                   .toList();

            for (ProductItemIngredient productItemIngredient : relatedIngredients) {
                double usedQuantity = (orderItem.getQuantity() * productItemIngredient.getQuantity());
                requiredQuantities.merge(productItemIngredient.getIngredient().getUuid(), usedQuantity, Double::sum);
            }
        }

        return requiredQuantities;*/
    }

    private String generateFiveLengthUniqueCode() {
        String base36;
        do {
            base36 = OrderCodeGenerator.getBase36(5);
        } while (orderPersistencePort.existsOrderByOrderCode(base36));
        return base36;
    }

    private void decreaseIngredientsStockOrder(List<OrderItem> orderItems, String orderCode) {

        Map<UUID, Double> stockAdjustments = calculateRequiredIngredients(orderItems);

        ingredientServicePort.batchAdjustStock(stockAdjustments, "Order placement for code " + orderCode);
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