package com.kaiho.gastromanager.application.order.mapper;

import com.kaiho.gastromanager.application.order.dto.request.OrderRequestDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponse;
import com.kaiho.gastromanager.application.orderitem.mapper.OrderItemMapper;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.model.OrderStatus;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.infrastructure.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    public OrderResponseDto toResponse(Order order) {
        if (order==null){
            return null;
        }
        List<OrderItemResponse> list = order.orderItems().stream().map(orderItemMapper::toResponse).toList();
        return OrderResponseDto.builder()
                .uuid(order.uuid())
                .userUuid(order.userUuid())
                .totalPrice(order.totalAmount())
                .status(order.status())
                .orderItems(list)
                .build();
    }

    public Order toDomain(OrderRequestDto orderRequestDto, UUID userUuid) {
        if (orderRequestDto == null){
            return null;
        }
        List<OrderItem> items = orderRequestDto.orderItems().stream()
                .map(orderItemMapper::toDomain)
                .toList();
        return Order.builder()
                .userUuid(userUuid)
                .customerNotes(orderRequestDto.customerNotes())
                .orderItems(items)
                .status(OrderStatus.PENDING)
                .build();
    }
}
