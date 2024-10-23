package com.kaiho.gastromanager.application.order.handler;

import com.kaiho.gastromanager.application.order.dto.request.OrderRequestDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.application.order.mapper.OrderMapper;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class OrderHandlerImpl implements OrderHandler {

    private final OrderServicePort orderServicePort;
    private final OrderMapper orderMapper;
    @Override
    public ApiGenericResponse<List<OrderResponseDto>> getAllOrders() {
        List<Order> orderList =  orderServicePort.getAllOrders();
        List<OrderResponseDto> orderResponseDtoList = orderList.stream().map(orderMapper::toResponse).toList();
        return buildSuccessResponse("List of orders retrieved successfully", orderResponseDtoList);
    }

    @Override
    public ApiGenericResponse<OrderResponseDto> getOrderByUUID(UUID orderUuid) {
        return null;
    }

    @Override
    public ApiGenericResponse<UUID> createOrder(OrderRequestDto orderRequestDto, UUID userUuid) {
        Order order = orderMapper.toDomain(orderRequestDto, userUuid);
        UUID orderUuid = orderServicePort.createOrder(order);
        return buildSuccessResponse("Order placed successfully", orderUuid);
    }

    @Override
    public ApiGenericResponse<OrderResponseDto> updateOrder(UUID orderUuid, OrderRequestDto orderRequestDto) {
        return null;
    }
}

