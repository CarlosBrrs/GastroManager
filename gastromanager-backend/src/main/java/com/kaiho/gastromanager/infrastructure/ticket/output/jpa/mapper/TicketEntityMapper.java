package com.kaiho.gastromanager.infrastructure.ticket.output.jpa.mapper;

import com.kaiho.gastromanager.domain.ticket.model.Ticket;
import com.kaiho.gastromanager.domain.ticket.model.TicketItem;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketEntityMapper {

    public Ticket toDomain(OrderEntity orderEntity) {
        List<TicketItem> ticketItems = orderEntity.getOrderItems().stream()
                                                  .map(this::toTicketItem)
                                                  .collect(Collectors.toList());

        // TODO: Calcular impuestos y propinas desde los pagos
        // Por ahora se calculan valores básicos
        BigDecimal subtotal = orderEntity.getTotalAmount();
        BigDecimal tax = BigDecimal.ZERO; // TODO: Obtener de TaxConfig o calcular desde payments
        BigDecimal tip = BigDecimal.ZERO; // TODO: Sumar tips de todos los payments

        // Construir el ticket
        return Ticket.builder()
                     .orderUuid(orderEntity.getUuid())
                     .orderCode(orderEntity.getCode())
                     .orderDate(orderEntity.getCreatedDate())
                     // Información del restaurante
                     .restaurantName(orderEntity.getRestaurant().getName())
                     .restaurantAddress(orderEntity.getRestaurant().getAddress())
                     .restaurantPhone("N/A") // TODO: Agregar teléfono a RestaurantEntity
                     // Información del cliente
                     .customerName(orderEntity.getCustomerName())
                     .tableNumber(orderEntity.getTableNumber())
                     .customerNotes(orderEntity.getCustomerNotes())
                     // Items
                     .items(ticketItems)
                     // Totales
                     .subtotal(subtotal)
                     .tax(tax)
                     .tip(tip)
                     .totalAmount(orderEntity.getTotalAmount())
                     .totalPaid(orderEntity.getTotalPaid())
                     .remainingToPay(orderEntity.getTotalAmount().subtract(orderEntity.getTotalPaid()))
                     // Estados
                     .paymentStatus(orderEntity.getPaymentStatus().toString())
                     .operationalStatus(orderEntity.getOperationalStatus().toString())
                     .build();
    }

    public TicketItem toTicketItem(OrderItemEntity itemEntity) {
        return TicketItem.builder()
                         .productName(itemEntity.getProduct().getName())
                         .quantity(itemEntity.getQuantity())
                         .unitPrice(itemEntity.getUnitPrice())
                         .subtotal(itemEntity.getSubtotal())
                         .notes(itemEntity.getCustomerNotes())
                         .build();
    }
}

