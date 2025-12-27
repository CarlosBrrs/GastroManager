package com.kaiho.gastromanager.application.order.mapper;

import com.kaiho.gastromanager.application.order.dto.request.ChangeOrderStatusRequestDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderCreateRequestDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderDetailResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderSummaryResponseDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponseDto;
import com.kaiho.gastromanager.application.orderitem.mapper.OrderItemMapper;
import com.kaiho.gastromanager.domain.order.model.ChangeOrderStatus;
import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.kaiho.gastromanager.domain.order.model.InvoicingStatus.NOT_INVOICED;
import static com.kaiho.gastromanager.domain.order.model.OperationalStatus.AWAITING_PAYMENT;
import static com.kaiho.gastromanager.domain.order.model.PaymentStatus.UNPAID;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static java.math.BigDecimal.ZERO;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderSummaryResponseDto toResponse(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderItemResponseDto> list = order.getOrderItems().stream().map(orderItemMapper::toResponse).toList();
        return OrderSummaryResponseDto.builder()
                                      .uuid(order.getUuid())
                                      .code(order.getCode())
                                      .totalAmount(order.getTotalAmount())
                                      .totalPaid(order.getTotalPaid())
                                      .remainingToPay(order.getRemainingToPay())
                                      .paymentStatus(order.getPaymentStatus())
                                      .operationalStatus(order.getOperationalStatus())
                                      .orderItems(list)
                                      .build();
    }

    public OrderDetailResponseDto toDetailResponse(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderItemResponseDto> list = order.getOrderItems().stream().map(orderItemMapper::toResponse).toList();

        return OrderDetailResponseDto.builder()
                                     .uuid(order.getUuid())
                                     .code(order.getCode())
                                     .createdBy(order.getCreatedBy())
                                     .customerName(order.getCustomerName())
                                     .customerNotes(order.getCustomerNotes())
                                     .tableNumber(order.getTableNumber())
                                     .totalAmount(order.getTotalAmount())
                                     .totalPaid(order.getTotalPaid())
                                     .remainingToPay(order.getRemainingToPay())
                                     .operationalStatus(order.getOperationalStatus())
                                     .paymentStatus(order.getPaymentStatus())  // Agregar el campo faltante
                                     .invoicingStatus(order.getInvoicingStatus())
                                     .orderItems(list)
                                     .updatedDate(order.getUpdatedDate())
                                     .invoices(List.of()) // Array vacío por ahora
                                     .requiresPaymentBefore(order.isRequiresPaymentBefore())
                                     .build();
    }

    public Order toDomain(OrderCreateRequestDto orderRequestDto) {
        if (orderRequestDto == null) {
            return null;
        }
        Restaurant restaurant = Restaurant.builder().uuid(getCurrentRestaurant()).build();

        List<OrderItem> orderItems = orderRequestDto.orderItems().stream().map(orderItemMapper::toDomain)
                                                    .toList();
        Order order = Order.builder()
                           .customerNotes(orderRequestDto.customerNotes())
                           .orderItems(orderItems)
                           .operationalStatus(AWAITING_PAYMENT)
                           .invoicingStatus(NOT_INVOICED)
                           .totalPaid(ZERO)
                           .paymentStatus(UNPAID)
                           .restaurant(restaurant)
                           .tableNumber(orderRequestDto.tableNumber())
                           .customerName(orderRequestDto.customerName())
                           .build();
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        return order;
    }

    public ChangeOrderStatus toChangeOrderStatus(ChangeOrderStatusRequestDto changeOrderStatusRequestDto) {
        if (changeOrderStatusRequestDto == null) {
            return null;
        }
        return ChangeOrderStatus.builder()
                                .newStatus(OperationalStatus.valueOf(changeOrderStatusRequestDto.newStatus()))
                                .reason(changeOrderStatusRequestDto.reason())
                                .build();
    }
}
