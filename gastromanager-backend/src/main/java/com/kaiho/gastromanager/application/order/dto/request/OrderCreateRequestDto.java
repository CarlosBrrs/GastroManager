package com.kaiho.gastromanager.application.order.dto.request;

import com.kaiho.gastromanager.application.orderitem.dto.request.OrderItemRequestDto;
import com.kaiho.gastromanager.domain.order.model.PaymentType;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequestDto(
        @NotEmpty(message = "Order items cannot be 0") List<OrderItemRequestDto> orderItems,
        String customerNotes,
        String tableNumber,
        String customerName) {
}
