package com.kaiho.gastromanager.application.orderitem.mapper;

import com.kaiho.gastromanager.application.orderitem.dto.request.OrderItemRequestDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponseDto;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    public OrderItem toDomain(OrderItemRequestDto orderItemRequestDto) {
        if (orderItemRequestDto == null) {
            return null;
        }
        Product productItem = Product.builder()
                                     .uuid(orderItemRequestDto.productUuid())
                                     .build();
        return OrderItem.builder()
                        .product(productItem)
                        .quantity(orderItemRequestDto.quantity())
                        .customerNotes(orderItemRequestDto.customerNotes())
                        .build();
    }

    public OrderItemResponseDto toResponse(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        return OrderItemResponseDto.builder()
                                   .uuid(orderItem.getUuid())
                                   .productUuid(orderItem.getProduct().getUuid())
                                   .productName(orderItem.getProduct().getName())
                                   .unitPrice(orderItem.getUnitPrice())
                                   .subtotal(orderItem.getSubtotal())
                                   .quantity(orderItem.getQuantity())
                                   .customerNotes(orderItem.getCustomerNotes())
                                   .build();
    }
}
