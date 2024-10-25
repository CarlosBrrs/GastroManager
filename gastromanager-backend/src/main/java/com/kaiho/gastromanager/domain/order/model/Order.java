package com.kaiho.gastromanager.domain.order.model;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record Order(UUID uuid,
                    UUID userUuid,
                    String customerNotes,
                    List<OrderItem> orderItems,
                    double totalAmount,
                    OrderStatus status,
                    String createdBy,
                    Instant createdDate,
                    String updatedBy,
                    Instant updatedDate) {
}
