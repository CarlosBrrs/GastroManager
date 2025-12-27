package com.kaiho.gastromanager.application.order.handler;

import com.kaiho.gastromanager.application.order.dto.request.ChangeOrderStatusRequestDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderCreateRequestDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderDetailResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderSummaryResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.UninvoicedItemResponseDto;
import com.kaiho.gastromanager.application.order.mapper.OrderMapper;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.model.ChangeOrderStatus;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.model.UninvoicedItemDto;
import com.kaiho.gastromanager.domain.orderitem.api.OrderItemServicePort;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@RequiredArgsConstructor
@Service
public class OrderHandlerImpl implements OrderHandler {

    private final OrderServicePort orderServicePort;
    private final OrderItemServicePort orderItemServicePort;
    private final OrderMapper orderMapper;

    @Override
    public ApiGenericResponse<Page<OrderSummaryResponseDto>> getAllOrders(OrderSearchCriteria criteria) {
        Page<Order> orderList = orderServicePort.getAllOrders(criteria);
        Page<OrderSummaryResponseDto> orderResponseDtoList = orderList.map(orderMapper::toResponse);
        return buildSuccessResponse("List of orders retrieved successfully", orderResponseDtoList);
    }

    @Override
    public ApiGenericResponse<OrderDetailResponseDto> getOrderByUUID(UUID orderUuid) {
        Order orderByUUID = orderServicePort.getOrderByUUID(orderUuid, getCurrentRestaurant());
        OrderDetailResponseDto response = orderMapper.toDetailResponse(orderByUUID);
        return buildSuccessResponse("Order retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<UUID> createOrder(OrderCreateRequestDto orderRequestDto) {
        Order order = orderMapper.toDomain(orderRequestDto);

        UUID orderUuid = orderServicePort.createOrder(order);

        return buildSuccessResponse("Order placed successfully", orderUuid);
    }

    @Override
    public ApiGenericResponse<OrderResponseDto> updateOrder(UUID orderUuid, OrderCreateRequestDto orderRequestDto) {
        return null;
    }

    @Override
    public ApiGenericResponse<UUID> changeOrderStatus(UUID orderUuid, ChangeOrderStatusRequestDto changeOrderStatusRequestDto, UUID userUuid) {
        ChangeOrderStatus orderStatus = orderMapper.toChangeOrderStatus(changeOrderStatusRequestDto);
        UUID orderUuidChanged = orderServicePort.changeOrderStatus(orderUuid, orderStatus);
        return buildSuccessResponse("Order operationalStatus changed successfully", orderUuidChanged);

    }

    @Override
    public ApiGenericResponse<List<UninvoicedItemResponseDto>> getUninvoicedItemsByOrderUuid(UUID orderUuid) {
        List<UninvoicedItemDto> uninvoicedItemsMap = orderItemServicePort.getUninvoicedItemsByOrderUuid(orderUuid);

        List<UninvoicedItemResponseDto> uninvoicedItemResponseDtoList = new ArrayList<>();


        return buildSuccessResponse("List of pending order items retrieved successfully", uninvoicedItemResponseDtoList);
    }
}
