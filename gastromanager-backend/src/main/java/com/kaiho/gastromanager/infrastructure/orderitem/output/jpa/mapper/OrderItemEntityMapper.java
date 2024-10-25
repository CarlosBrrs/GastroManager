package com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemEntityMapper {
    public OrderItemEntity toEntity(OrderItem orderItem) {
        if (orderItem== null){
            return null;
        }
        return OrderItemEntity.builder()
                .unitPrice(orderItem.unitPrice())
                .quantity(orderItem.quantity())
                .build();
    }

    public OrderItem toDomain(OrderItemEntity orderItemEntity) {
        if (orderItemEntity== null) {
            return null;
        }
        return OrderItem.builder()
                .quantity(orderItemEntity.getQuantity())
                .unitPrice(orderItemEntity.getUnitPrice())
                .productItemUuid(orderItemEntity.getProductItem().getUuid())
                .build();
    }
}
