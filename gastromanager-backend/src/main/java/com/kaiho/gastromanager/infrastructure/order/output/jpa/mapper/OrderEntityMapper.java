package com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper.OrderItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
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
        List<OrderItem> orderItems = orderEntity.getOrderItems().stream()
                                                .map(orderItemEntityMapper::toDomain).toList();
        Restaurant restaurant = Restaurant.builder()
                                          .uuid(orderEntity.getRestaurant().getUuid())
                                          .build();
        return Order.builder()
                    .uuid(orderEntity.getUuid())
                    .code(orderEntity.getCode())
                    .updatedDate(orderEntity.getUpdatedDate())
                    .invoicingStatus(orderEntity.getInvoicingStatus())
                    .customerNotes(orderEntity.getCustomerNotes())
                    .totalAmount(orderEntity.getTotalAmount())
                    .totalPaid(orderEntity.getTotalPaid())
                    .restaurant(restaurant)
                    .requiresPaymentBefore(orderEntity.getRequiresPaymentBeforeOrder())
                    .operationalStatus(orderEntity.getOperationalStatus())
                    .paymentStatus(orderEntity.getPaymentStatus())
                    .remainingToPay(orderEntity.getTotalAmount().subtract(orderEntity.getTotalPaid()))
                    .customerNotes(orderEntity.getCustomerNotes())

                    .orderItems(orderItems)
                    .createdBy(orderEntity.getCreatedBy())
                    .createdDate(orderEntity.getCreatedDate())
                    .updatedBy(orderEntity.getUpdatedBy())
                    .updatedDate(orderEntity.getUpdatedDate())
                    .tableNumber(orderEntity.getTableNumber())
                    .customerName(orderEntity.getCustomerName())
                    .build();
    }

    public OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                                                       .map(orderItemEntityMapper::toEntity)
                                                       .toList();
        OrderEntity orderEntity = OrderEntity.builder()
                                             .uuid(order.getUuid())
                                             .code(order.getCode())
                                             .customerNotes(order.getCustomerNotes())
                                             .orderItems(new ArrayList<>())
                                             .totalAmount(order.getTotalAmount())
                                             .totalPaid(order.getTotalPaid())
                                             .requiresPaymentBeforeOrder(order.isRequiresPaymentBefore())
                                             .operationalStatus(order.getOperationalStatus())
                                             .paymentStatus(order.getPaymentStatus())
                                             .invoicingStatus(order.getInvoicingStatus())
                                             .restaurant(RestaurantEntity.builder().uuid(order.getRestaurant().getUuid()).build())
                                             .tableNumber(order.getTableNumber())
                                             .customerName(order.getCustomerName())
                                             .build();
        orderItemEntities.forEach(orderEntity::addOrderItem);
        return orderEntity;
    }
}
