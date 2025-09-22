package com.kaiho.gastromanager.domain.order.model;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID uuid;
    private String code;
    private Restaurant restaurant;
    private String customerNotes;
    private List<OrderItem> orderItems;
    private boolean requiresPaymentBefore;
    private BigDecimal totalAmount;
    private BigDecimal totalPaid;
    private BigDecimal remainingToPay;
    private OperationalStatus operationalStatus;
    private PaymentStatus paymentStatus;
    private InvoicingStatus invoicingStatus;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;
    private String tableNumber;
    private String customerName;

}
