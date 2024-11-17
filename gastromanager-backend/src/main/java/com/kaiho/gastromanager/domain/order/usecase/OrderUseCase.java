package com.kaiho.gastromanager.domain.order.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitemingredient.api.ProductItemIngredientServicePort;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<Order> getAllOrders() {
        return orderPersistencePort.findAllOrders();
    }

    @Override
    public UUID createOrder(Order order) {
        // buscar el unitprice para cada orderitem en base de datos

        // TODO: No se va a necesitar porque se va a validar el request que la lista sea > 0
        if (order.orderItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be 0");
        }
        List<UUID> productItemUuids = order.orderItems().stream()
                .map(OrderItem::productItemUuid) // Obtén los UUIDs de los productos
                .toList();

        List<ProductItem> productItemList = productItemServicePort.getAllProductItemsByUuid(productItemUuids);
        Map<UUID, Double> productItemPriceMap = productItemList.stream()
                .collect(Collectors.toMap(ProductItem::uuid, ProductItem::price)); // Crear un mapa de precios

        // Calcular el total de la orden
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

        }
        Order orderWithAmount = Order.builder()
                .userUuid(order.userUuid())
                .customerNotes(order.customerNotes())
                .status(order.status())
                .totalAmount(totalAmount)
                .orderItems(orderItemsWithPrices)
                .build();

        // TODO: Verificar que se cuenta con el stock necesario para crear la orden
        // TODO: Esto tal vez deberia hacerse antes de hacer todo el proceso de creacion de orden, deshabilitar productos que no tengan suficiente stock

        UUID placedOrderUuid = orderPersistencePort.createOrder(orderWithAmount).uuid();

        //TODO: Este ajuste se deberia hacer cuando la orden se inicie a preparar en cocina, no cuando se coloque la orden
        decreaseIngredientsStockOrder(orderWithAmount.orderItems());
        return placedOrderUuid;

    }

    private void decreaseIngredientsStockOrder(List<OrderItem> orderItems) {
// Obtener los UUIDs de los productos ordenados
        List<UUID> productItemUuids = orderItems.stream()
                .map(OrderItem::productItemUuid)
                .toList();

        // Consultar la tabla productitemingredient para obtener las relaciones
        List<ProductItemIngredient> productItemIngredients = productItemIngredientServicePort.getByProductItemUuids(productItemUuids);

        // Disminuir el stock de los ingredientes
        for (OrderItem orderItem : orderItems) {
            // Encontrar las relaciones de ingredientes correspondientes al ProductItem de este OrderItem
            List<ProductItemIngredient> relatedIngredients = productItemIngredients.stream()
                    .filter(ingredient -> ingredient.productItemUuid().equals(orderItem.productItemUuid()))
                    .toList();

            // Disminuir el stock de cada ingrediente relacionado
            for (ProductItemIngredient productItemIngredient : relatedIngredients) {
                Ingredient ingredient = ingredientServicePort.getIngredientById(productItemIngredient.ingredientUuid());
                double quantityToDecrease = orderItem.quantity() * productItemIngredient.quantity();  // Cantidad a reducir por el multiplicador
                ingredientServicePort.adjustIngredientStock(productItemIngredient.ingredientUuid(), (int) (ingredient.availableStock() - quantityToDecrease));
            }
        }
    }
}
