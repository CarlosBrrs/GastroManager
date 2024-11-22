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

        // TODO: No se va a necesitar porque se va a validar el request que la lista sea > 0
        if (order.orderItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be 0");
        }

/*        // Validar stock de ingredientes antes de procesar la orden
        validateIngredientsStock(order.orderItems());*/

        // Obtén los UUIDs de los productos
        List<UUID> productItemUuids = order.orderItems().stream()
                .map(OrderItem::productItemUuid)
                .toList();

        List<ProductItem> productItemList = productItemServicePort.getAllProductItemsByUuid(productItemUuids);

        // Crear un mapa de precios, cuanto cuesta cada product item en el sistema
        Map<UUID, Double> productItemPriceMap = productItemList.stream()
                .collect(Collectors.toMap(ProductItem::uuid, ProductItem::price));

        // Validar el stock de ingredientes
        validateIngredientsStock(order.orderItems());

        // Calcular el total de la orden usando el PricingService usando cada orderItem y el precio que tiene cada producto
        double totalAmount = pricingServicePort.calculateOrderTotal(order.orderItems(), productItemPriceMap);

        /*// Calcular el total de la orden
        double totalAmount = 0.0;

        //Here would go any logic to apply general discounts, or be more detailed about the order rubrics
        List<OrderItem> orderItemsWithPrices = new ArrayList<>();
        for (OrderItem orderItem : order.orderItems()) {
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

            totalAmount += unitPrice * orderItem.quantity();

        }*/
        List<OrderItem> orderItemsWithPrices = addPricesToOrderItems(order.orderItems(), productItemPriceMap);
        String orderCode = generateFiveLengthUniqueCode();

        Order orderWithAmount = Order.builder()
                .userUuid(order.userUuid())
                .orderCode(orderCode)
                .customerNotes(order.customerNotes())
                .status(order.status())
                .totalAmount(totalAmount)
                .orderItems(orderItemsWithPrices) // Items sin stock reducido aún
                .build();


        // TODO: Verificar que se cuenta con el stock necesario para crear la orden
        // TODO: Esto tal vez deberia hacerse antes de hacer todo el proceso de creacion de orden, deshabilitar productos que no tengan suficiente stock

        // Persistir la orden
        UUID placedOrderUuid = orderPersistencePort.createOrder(orderWithAmount).uuid();

        //TODO: Este ajuste se deberia hacer cuando la orden se inicie a preparar en cocina, no cuando se coloque la orden
        // Disminuir el stock de los ingredientes
        decreaseIngredientsStockOrder(orderWithAmount.orderItems() , orderCode);
        return placedOrderUuid;

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
            if (ingredient.availableStock() < requiredQuantity) {
                throw new InsufficientStockException(ingredient.name(), requiredQuantity, ingredient.availableStock());
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
        ingredientServicePort.batchAdjustStock(stockAdjustments, "Order placement for code "+ orderCode);
    }
}
