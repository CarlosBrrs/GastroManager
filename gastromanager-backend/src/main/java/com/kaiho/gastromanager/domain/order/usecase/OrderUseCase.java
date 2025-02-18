package com.kaiho.gastromanager.domain.order.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.exception.InsufficientStockException;
import com.kaiho.gastromanager.domain.order.exception.OrderDoesNotExistException;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.pricing.api.PricingServicePort;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
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
    private final IngredientServicePort ingredientServicePort;
    private final PricingServicePort pricingServicePort;

    @Override
    public List<Order> getAllOrders() {
        return orderPersistencePort.findAllOrders();
    }

    @Override
    @Transactional
    public UUID createOrder(Order order) {

        // TODO: No se va a necesitar porque se va a validar el request que la lista sea > 0
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be 0");
        }
        // TODO: No se va a necesitar porque se va a validar el request que la lista sea > 0

        validateIngredientsStock(order.getOrderItems());

        List<ProductItem> productItemList = order.getOrderItems().stream()
                .map(OrderItem::getProductItem)
                .toList();

        Map<UUID, Double> productItemPriceMap = productItemList.stream()
                .collect(Collectors.toMap(ProductItem::getUuid, ProductItem::getPrice));

        // Calcular el total de la orden usando el PricingService usando cada orderItem y el precio que tiene cada producto
        // Este totalAmount se calcula leyendo el precio en la tabla de productitem, luego de esto deberia aplicar descuentos si aplica
        double totalAmount = pricingServicePort.calculateOrderTotal(order.getOrderItems(), productItemPriceMap);

        // aqui se agrega para enviar una orden con toda la informacion necesaria, aunque primero deberia calcular el precio de cada producto, aplicar descuentos y luego calcular el totalAmount
        List<OrderItem> orderItemsWithPrices = addPricesToOrderItems(order.getOrderItems(), productItemPriceMap);
        String orderCode = generateFiveLengthUniqueCode();

        order.setCode(orderCode);
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItemsWithPrices);

        // TODO: Verificar que se cuenta con el stock necesario para crear la orden
        // TODO: Esto tal vez deberia hacerse antes de hacer todo el proceso de creacion de orden, deshabilitar productos que no tengan suficiente stock

        UUID placedOrderUuid = orderPersistencePort.createOrder(order).getUuid();

        //TODO: Este ajuste se deberia hacer cuando la orden se inicie a preparar en cocina, no cuando se coloque la orden
        // Disminuir el stock de los ingredientes
        decreaseIngredientsStockOrder(order.getOrderItems(), orderCode);
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

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByUUID(UUID orderUuid, UUID restaurantUuid) {
        return orderPersistencePort.findOrderByUuid(orderUuid, restaurantUuid)
                .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));
    }

    //TODO: Modify the list and return void
    private List<OrderItem> addPricesToOrderItems(List<OrderItem> orderItems, Map<UUID, Double> productItemPriceMap) {
        List<OrderItem> orderItemsWithPrices = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Double unitPrice = productItemPriceMap.get(orderItem.getProductItem().getUuid());
            if (unitPrice == null) {
                throw new IllegalArgumentException("El producto con UUID " + orderItem.getProductItem().getUuid() + " no se encuentra disponible.");
            }
            orderItem.setUnitPrice(unitPrice);
            orderItemsWithPrices.add(orderItem);
        }
        return orderItemsWithPrices;
    }

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

        List<ProductItemIngredient> productItemIngredients = orderItems.stream()
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

        Map<UUID, Double> stockAdjustments = calculateRequiredIngredients(orderItems);

        ingredientServicePort.batchAdjustStock(stockAdjustments, "Order placement for code " + orderCode);
    }
}
