package com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.mapper.ProductItemEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemEntityMapper {

    private final ProductItemEntityMapper productItemEntityMapper;

    public OrderItemEntity toEntity(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        ProductItemEntity productItemEntity = productItemEntityMapper.toEntity(orderItem.getProductItem());
        return OrderItemEntity.builder()
                .productItem(productItemEntity)
                .unitPrice(orderItem.getUnitPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem toDomain(OrderItemEntity orderItemEntity) {
        if (orderItemEntity == null) {
            return null;
        }

        ProductItem productItem = productItemEntityMapper.toDomain(orderItemEntity.getProductItem());
        return OrderItem.builder()
                .quantity(orderItemEntity.getQuantity())
                .unitPrice(orderItemEntity.getUnitPrice())
                .productItem(productItem)
                .build();
    }
}
