package com.kaiho.gastromanager.application.order.dto.response;

import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponseDto;
import com.kaiho.gastromanager.domain.order.model.OrderStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponseDto(UUID uuid, UUID userUuid, String code, String user, double totalPrice,
                               OrderStatus status,
                               List<OrderItemResponseDto> orderItems, Instant updatedDate) {
}
