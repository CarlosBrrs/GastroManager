package com.kaiho.gastromanager.domain.pricing.api;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PricingServicePort {
    double calculateOrderTotal(List<OrderItem> orderItems, Map<UUID, Double> productItemPriceMap);
}
