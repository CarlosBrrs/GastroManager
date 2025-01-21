package com.kaiho.gastromanager.domain.order.model;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID uuid;
    private String orderCode;
    private UUID userUuid;
    private UUID restaurantUuid;
    private String customerNotes;
    private List<OrderItem> orderItems;
    private double totalAmount;
    private OrderStatus status;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;

}
