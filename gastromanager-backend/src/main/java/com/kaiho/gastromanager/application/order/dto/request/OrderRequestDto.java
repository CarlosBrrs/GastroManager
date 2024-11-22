package com.kaiho.gastromanager.application.order.dto.request;

import com.kaiho.gastromanager.application.orderitem.dto.request.OrderItemRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

public record OrderRequestDto(

        @NotEmpty(message = "Order items cannot be 0")
        List<OrderItemRequestDto> orderItems,
        String customerNotes) {
}
