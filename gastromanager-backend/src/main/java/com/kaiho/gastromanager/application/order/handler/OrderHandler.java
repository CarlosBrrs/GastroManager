package com.kaiho.gastromanager.application.order.handler;

import com.kaiho.gastromanager.application.order.dto.request.OrderRequestDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface OrderHandler {
    ApiGenericResponse<List<OrderResponseDto>> getAllOrders();

    ApiGenericResponse<OrderResponseDto> getOrderByUUID(UUID orderUuid);

    ApiGenericResponse<UUID> createOrder(OrderRequestDto orderRequestDto, UUID userUuid);

    ApiGenericResponse<OrderResponseDto> updateOrder(UUID orderUuid, OrderRequestDto orderRequestDto);
}
