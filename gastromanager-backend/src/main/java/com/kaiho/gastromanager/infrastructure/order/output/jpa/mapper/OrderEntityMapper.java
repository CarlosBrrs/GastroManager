package com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.model.OrderStatus;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper.OrderItemEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEntityMapper {

    private final OrderItemEntityMapper orderItemEntityMapper;

    public Order toDomain(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }
        List<OrderItem> orderItems = orderEntity.getOrderItems().stream().map(orderItemEntityMapper::toDomain).toList();
        return Order.builder()
                .uuid(orderEntity.getUuid())
                .orderCode(orderEntity.getCode())
                .userUuid(orderEntity.getUser().getUuid())
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
        if (order==null){
            return null;
        }
        return OrderEntity.builder()
                .code(order.getOrderCode())
                .totalPrice(order.getTotalAmount())
                .status(order.getStatus())
                .orderItems(new ArrayList<>())
                .customerNotes(order.getCustomerNotes())
                .build();
    }
}
