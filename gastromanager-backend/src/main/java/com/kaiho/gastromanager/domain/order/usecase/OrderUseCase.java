package com.kaiho.gastromanager.domain.order.usecase;

import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.spi.OrderPersistencePort;
import com.kaiho.gastromanager.domain.orderitem.api.OrderItemServicePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
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

        return orderPersistencePort.createOrder(orderWithAmount).uuid();

    }
}
