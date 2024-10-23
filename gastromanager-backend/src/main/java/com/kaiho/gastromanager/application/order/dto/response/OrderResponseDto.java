package com.kaiho.gastromanager.application.order.dto.response;

import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponse;
import com.kaiho.gastromanager.domain.order.model.OrderStatus;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponseDto(UUID uuid, UUID userUuid, double totalPrice, OrderStatus status, List<OrderItemResponse> orderItems) {
}
