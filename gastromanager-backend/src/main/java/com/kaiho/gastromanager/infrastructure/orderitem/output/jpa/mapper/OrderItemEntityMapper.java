package com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.mapper.ProductItemEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemEntityMapper {

    public OrderItemEntity toEntity(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        ProductEntity productEntity = ProductEntity.builder().uuid(orderItem.getProduct().getUuid()).build();
        return OrderItemEntity.builder()
                              .product(productEntity)
                              .unitPrice(orderItem.getUnitPrice())
                              .quantity(orderItem.getQuantity())
                              .subtotal(orderItem.getSubtotal())
                              .customerNotes(orderItem.getCustomerNotes())
                              .build();
    }

    public OrderItem toDomain(OrderItemEntity orderItemEntity) {
        if (orderItemEntity == null) {
            return null;
        }

        return OrderItem.builder()
                        .uuid(orderItemEntity.getUuid())
                        .quantity(orderItemEntity.getQuantity())
                        .unitPrice(orderItemEntity.getUnitPrice())
                        .subtotal(orderItemEntity.getSubtotal())
                        .customerNotes(orderItemEntity.getCustomerNotes())
                        .product(Product.builder()
                                        .uuid(orderItemEntity.getProduct().getUuid())
                                        .name(orderItemEntity.getProduct().getName())
                                        .build())
                        .build();
    }
}
