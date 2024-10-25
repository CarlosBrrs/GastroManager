package com.kaiho.gastromanager.domain.order.api;

import com.kaiho.gastromanager.domain.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderServicePort {
    List<Order> getAllOrders();

    UUID createOrder(Order order);
}
