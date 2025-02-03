package com.kaiho.gastromanager.application.order.mapper;

import com.kaiho.gastromanager.application.order.dto.request.OrderRequestDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponse;
import com.kaiho.gastromanager.application.orderitem.mapper.OrderItemMapper;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.model.OrderStatus;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final RestaurantServicePort restaurantServicePort;

    public OrderResponseDto toResponse(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderItemResponse> list = order.getOrderItems().stream().map(orderItemMapper::toResponse).toList();
        return OrderResponseDto.builder()
                .uuid(order.getUuid())
                .userUuid(order.getUser().getUuid())
                .totalPrice(order.getTotalAmount())
                .status(order.getStatus())
                .orderItems(list)
                .build();
    }

    public Order toDomain(OrderRequestDto orderRequestDto) {
        if (orderRequestDto == null) {
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Restaurant restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());

        List<OrderItem> orderItems = orderRequestDto.orderItems().stream().map(orderItemMapper::toDomain)
                .toList();
        return Order.builder()
                .user(principal)
                .customerNotes(orderRequestDto.customerNotes())
                .orderItems(orderItems)
                .status(OrderStatus.AWAITING_PAYMENT)
                .restaurant(restaurant)
                .build();
    }
}
