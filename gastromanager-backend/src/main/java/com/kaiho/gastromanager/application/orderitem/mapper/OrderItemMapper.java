package com.kaiho.gastromanager.application.orderitem.mapper;

import com.kaiho.gastromanager.application.orderitem.dto.request.OrderItemRequestDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponse;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItem toDomain(OrderItemRequestDto orderItemRequestDto) {
        if (orderItemRequestDto == null) {
            return null;
        }
        return OrderItem.builder()
                .productItemUuid(orderItemRequestDto.productItemUuid())
                .quantity(orderItemRequestDto.quantity())
                .build();
    }

    public OrderItemResponse toResponse(OrderItem orderItem) {
        if (orderItem==null) {
            return null;
        }
        return OrderItemResponse.builder()
                .productItemUuid(orderItem.productItemUuid())
                .quantity(orderItem.quantity())
                .unitPrice(orderItem.unitPrice())
                .build();
    }
}
