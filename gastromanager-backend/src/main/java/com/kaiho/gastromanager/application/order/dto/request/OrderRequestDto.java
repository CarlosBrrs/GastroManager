package com.kaiho.gastromanager.application.order.dto.request;

import com.kaiho.gastromanager.application.orderitem.dto.request.OrderItemRequestDto;
import lombok.Builder;

import java.util.List;

public record OrderRequestDto(List<OrderItemRequestDto> orderItems, String customerNotes) {
}
