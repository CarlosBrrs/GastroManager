package com.kaiho.gastromanager.application.orderitem.mapper;

import com.kaiho.gastromanager.application.orderitem.dto.request.OrderItemRequestDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponseDto;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private final ProductItemServicePort productItemServicePort;

    public OrderItem toDomain(OrderItemRequestDto orderItemRequestDto) {
        if (orderItemRequestDto == null) {
            return null;
        }
        ProductItem productItem = productItemServicePort.getProductItemByUUID(orderItemRequestDto.productItemUuid(), getCurrentRestaurant());
        return OrderItem.builder()
                .productItem(productItem)
                .quantity(orderItemRequestDto.quantity())
                .build();
    }

    public OrderItemResponseDto toResponse(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        return OrderItemResponseDto.builder()
                .productItemUuid(orderItem.getProductItem().getUuid())
                .productItemName(orderItem.getProductItem().getName())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .build();
    }
}
