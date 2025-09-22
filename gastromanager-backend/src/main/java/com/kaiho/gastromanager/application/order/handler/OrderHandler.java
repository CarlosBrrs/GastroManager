package com.kaiho.gastromanager.application.order.handler;

import com.kaiho.gastromanager.application.order.dto.request.ChangeOrderStatusRequestDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderDetailResponseDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderCreateRequestDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderSummaryResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.UninvoicedItemResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface OrderHandler {
    ApiGenericResponse<Page<OrderSummaryResponseDto>> getAllOrders(OrderSearchCriteria criteria);

    ApiGenericResponse<OrderDetailResponseDto> getOrderByUUID(UUID orderUuid);

    ApiGenericResponse<UUID> createOrder(OrderCreateRequestDto orderRequestDto);

    ApiGenericResponse<OrderResponseDto> updateOrder(UUID orderUuid, OrderCreateRequestDto orderRequestDto);

    ApiGenericResponse<UUID> changeOrderStatus(UUID orderUuid, ChangeOrderStatusRequestDto changeOrderStatusRequestDto, UUID userUuid);

    ApiGenericResponse<List<UninvoicedItemResponseDto>> getUninvoicedItemsByOrderUuid(UUID orderUuid);
}
