package com.kaiho.gastromanager.domain.order.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.exception.InsufficientStockException;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.pricing.api.PricingServicePort;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitemingredient.api.ProductItemIngredientServicePort;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.config.generator.OrderCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderUseCase implements OrderServicePort {

    private final OrderPersistencePort orderPersistencePort;
    private final ProductItemServicePort productItemServicePort;
    private final ProductItemIngredientServicePort productItemIngredientServicePort;
    private final IngredientServicePort ingredientServicePort;
    private final PricingServicePort pricingServicePort;

    @Override
    public List<Order> getAllOrders() {
        return orderPersistencePort.findAllOrders();
    }

    @Override
    @Transactional
    public UUID createOrder(Order order) {
/*        UUID restaurantUuid = userPersistencePort.getRestaurantByUserUuid(order.getUserUuid()).orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado")).getUuid();
        RestaurantConfig config = restaurantPersistencePort.getRestaurantConfig(resturantUuid).orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada para el restaurante"));

// Seleccionar la estrategia de colocación
        OrderPlacementStrategy strategy = placementStrategyFactory.getStrategy(config);*/

        // TODO: No se va a necesitar porque se va a validar el request que la lista sea > 0
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be 0");
        }

        validateIngredientsStock(order.getOrderItems());

        List<UUID> productItemUuids = order.getOrderItems().stream()
                .map(OrderItem::productItemUuid)
                .toList();

        List<ProductItem> productItemList = productItemServicePort.getAllProductItemsByUuid(productItemUuids);

        Map<UUID, Double> productItemPriceMap = productItemList.stream()
                .collect(Collectors.toMap(ProductItem::uuid, ProductItem::price));

        // Calcular el total de la orden usando el PricingService usando cada orderItem y el precio que tiene cada producto
        double totalAmount = pricingServicePort.calculateOrderTotal(order.getOrderItems(), productItemPriceMap);

        List<OrderItem> orderItemsWithPrices = addPricesToOrderItems(order.getOrderItems(), productItemPriceMap);
        String orderCode = generateFiveLengthUniqueCode();

        Order orderWithAmount = Order.builder()
                .userUuid(order.getUserUuid())
                .orderCode(orderCode)
                .customerNotes(order.getCustomerNotes())
                .status(order.getStatus())
                .totalAmount(totalAmount)
                .orderItems(orderItemsWithPrices)
                .build();


        // TODO: Verificar que se cuenta con el stock necesario para crear la orden
        // TODO: Esto tal vez deberia hacerse antes de hacer todo el proceso de creacion de orden, deshabilitar productos que no tengan suficiente stock

        // Persistir la orden
        UUID placedOrderUuid = orderPersistencePort.createOrder(orderWithAmount).getUuid();

        //TODO: Este ajuste se deberia hacer cuando la orden se inicie a preparar en cocina, no cuando se coloque la orden
        // Disminuir el stock de los ingredientes
        decreaseIngredientsStockOrder(orderWithAmount.getOrderItems(), orderCode);
        return placedOrderUuid;

    }

    @Override
    public UUID changeOrderStatus(UUID orderUuid, String newStatus, String reason, UUID userUuid) {
        /*Order order = orderPersistencePort.getOrderByUuid(orderUuid)
                .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));

        User user = userPersistencePort.getUserByUuid(userUuid)
                .orElseThrow(() -> new UserDoesNotExistException(userUuid));

        // TODO: Change to database impl orderstatus
        if (!isTransitionAllowed(order.getStatus().name(), newStatus, user.roles())) {
            throw new UnauthorizedOrderStatusChangeException(userUuid, newStatus);
        }

        return orderPersistencePort.changeOrderStatus(orderUuid, newStatus, reason);*/
        return null;
    }

    private List<OrderItem> addPricesToOrderItems(List<OrderItem> orderItems, Map<UUID, Double> productItemPriceMap) {
        List<OrderItem> orderItemsWithPrices = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Double unitPrice = productItemPriceMap.get(orderItem.productItemUuid());
            if (unitPrice == null) {
                throw new IllegalArgumentException("El producto con UUID " + orderItem.productItemUuid() + " no se encuentra disponible.");
            }
            OrderItem itemWithPrice = OrderItem.builder()
                    .productItemUuid(orderItem.productItemUuid())
                    .unitPrice(unitPrice)
                    .quantity(orderItem.quantity())
                    .build();
            orderItemsWithPrices.add(itemWithPrice);
        }
        return orderItemsWithPrices;
    }

    private void validateIngredientsStock(List<OrderItem> orderItems) {

        // Obtener cantidades necesarias por ingrediente en la orden
        Map<UUID, Integer> requiredIngredientQuantities = calculateRequiredIngredients(orderItems);

        // Consultar todos los ingredientes necesarios de una vez
        Map<UUID, Ingredient> ingredientMap = ingredientServicePort.getIngredientsByUuids(requiredIngredientQuantities.keySet());

        // Para cada registro del mapa de ingredientes
        for (Map.Entry<UUID, Integer> entry : requiredIngredientQuantities.entrySet()) {
            UUID ingredientUuid = entry.getKey();
            int requiredQuantity = entry.getValue();

            Ingredient ingredient = ingredientMap.get(ingredientUuid);
            // Si tengo menos stock del que quiero usar para la orden lanzar excepcion
            if (ingredient.getAvailableStock() < requiredQuantity) {
                throw new InsufficientStockException(ingredient.getName(), requiredQuantity, ingredient.getAvailableStock());
            }
        }
    }

    //the uuid of the ingredient and the quantity to decrease, or how much it is used in the order according to the recipe
    private Map<UUID, Integer> calculateRequiredIngredients(List<OrderItem> orderItems) {
        List<UUID> productItemUuids = orderItems.stream()
                .map(OrderItem::productItemUuid)
                .toList();

        List<ProductItemIngredient> productItemIngredients = productItemIngredientServicePort.getByProductItemUuids(productItemUuids);

        Map<UUID, Integer> requiredQuantities = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            List<ProductItemIngredient> relatedIngredients = productItemIngredients.stream()
                    .filter(ingredient -> ingredient.productItemUuid().equals(orderItem.productItemUuid()))
                    .toList();

            for (ProductItemIngredient productItemIngredient : relatedIngredients) {
                int usedQuantity = (int) (orderItem.quantity() * productItemIngredient.quantity());
                requiredQuantities.merge(productItemIngredient.ingredientUuid(), usedQuantity, Integer::sum);
            }
        }
        return requiredQuantities;
    }

    private String generateFiveLengthUniqueCode() {
        String base36;
        do {
            base36 = OrderCodeGenerator.getBase36(5);
        } while (orderPersistencePort.existsOrderByOrderCode(base36));
        return base36;
    }

    private void decreaseIngredientsStockOrder(List<OrderItem> orderItems, String orderCode) {
        // Calcular ajustes de stock
        //the uuid of the ingredient and the quantity to decrease, or how much it is used in the order
        Map<UUID, Integer> stockAdjustments = calculateRequiredIngredients(orderItems);

        // Enviar ajustes al servicio para procesamiento en lote
        ingredientServicePort.batchAdjustStock(stockAdjustments, "Order placement for code " + orderCode);
    }

/*    private boolean isTransitionAllowed(String currentStatus, String newStatus, Set<Role> userRoles) {
        Map<String, Map<String, List<String>>> transitions = Map.of(
                "PENDING", Map.of(
                        "PREPARING", List.of("Manager", "Waiter"),
                        "CANCELLED", List.of("Manager")
                ),
                "PREPARING", Map.of(
                        "READY", List.of("Chef", "KitchenStaff"),
                        "PENDING", List.of("Manager"),
                        "CANCELLED", List.of("Manager")
                ),
                "READY", Map.of(
                        "ON_TABLE", List.of("Waiter"),
                        "CANCELLED", List.of("Manager")
                ),
                "ON_TABLE", Map.of(
                        "COMPLETED", List.of("Waiter", "Cashier"),
                        "CANCELLED", List.of("Manager")
                )
        );

        // Obtener la lista de roles autorizados para la transición
        List<String> allowedRoles = transitions
                .getOrDefault(currentStatus, Map.of())
                .getOrDefault(newStatus, List.of());

        // Validar si alguno de los roles del usuario está en la lista de roles permitidos
        return userRoles.stream()
                .map(Role::roleType) // Asumiendo que Role tiene un método `getName` que devuelve el nombre del rol
                .anyMatch(allowedRoles::contains);
    }*/
}
