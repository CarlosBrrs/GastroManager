package com.kaiho.gastromanager.domain.order.spi;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderPersistencePort {
    Page<Order> findAllOrders(OrderSearchCriteria criteria, Pageable pageable);

    Order createOrder(Order order);

    boolean existsOrderByOrderCode(String base36);

    Optional<Order> getOrderByUuid(UUID orderUuid, UUID restaurantUuid);

    UUID changeOrderStatus(UUID orderUuid, String newStatus, String reason);

    Optional<Order> findOrderByUuid(UUID orderUuid, UUID restaurantUuid);

    void updateOrder(Order order);
}
