package com.kaiho.gastromanager.domain.order.spi;

import com.kaiho.gastromanager.domain.order.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderPersistencePort {
    List<Order> findAllOrders();

    Order createOrder(Order order);

    boolean existsOrderByOrderCode(String base36);

    Optional<Order> getOrderByUuid(UUID orderUuid, UUID restaurantUuid);

    UUID changeOrderStatus(UUID orderUuid, String newStatus, String reason);

    Optional<Order> findOrderByUuid(UUID orderUuid, UUID restaurantUuid);
}
