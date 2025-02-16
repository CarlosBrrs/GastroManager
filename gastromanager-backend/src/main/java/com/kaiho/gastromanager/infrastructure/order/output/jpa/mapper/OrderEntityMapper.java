package com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper.OrderItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEntityMapper {

    private final OrderItemEntityMapper orderItemEntityMapper;
    private final UserEntityMapper userEntityMapper;

    public Order toDomain(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }
        List<OrderItem> orderItems = orderEntity.getOrderItems().stream()
                .map(orderItemEntityMapper::toDomain).toList();
        User user = userEntityMapper.toDomain(orderEntity.getUser());
        return Order.builder()
                .uuid(orderEntity.getUuid())
                .code(orderEntity.getCode())
                .updatedDate(orderEntity.getUpdatedDate())
                .user(user)
                .customerNotes(orderEntity.getCustomerNotes())
                .totalAmount(orderEntity.getTotalPrice())
                .status(orderEntity.getStatus())
                .orderItems(orderItems)
                .createdBy(orderEntity.getCreatedBy())
                .createdDate(orderEntity.getCreatedDate())
                .updatedBy(orderEntity.getUpdatedBy())
                .updatedDate(orderEntity.getUpdatedDate())
                .build();
    }

    public OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(orderItemEntityMapper::toEntity)
                .toList();
        OrderEntity build = OrderEntity.builder()
                .code(order.getCode())
                .totalPrice(order.getTotalAmount())
                .status(order.getStatus())
                .orderItems(new ArrayList<>())
                .customerNotes(order.getCustomerNotes())
                .build();
        orderItemEntities.forEach(build::addOrderItem);
        return build;
    }
}
