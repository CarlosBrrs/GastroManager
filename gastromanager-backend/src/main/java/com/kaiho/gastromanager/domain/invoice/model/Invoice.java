package com.kaiho.gastromanager.domain.invoice.model;

import com.kaiho.gastromanager.domain.invoiceitem.model.InvoiceItem;
import com.kaiho.gastromanager.domain.order.model.Order;
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
public class Invoice {
    private final UUID uuid;
    private final Restaurant restaurant;
    private final String createdBy;
    private final Instant createdDate;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private List<InvoiceItem> invoiceItems;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal tipAmount;
    private BigDecimal amount;
    private BigDecimal discount;
    private double taxRate;
    private Order order;
    private PaymentStatus status;
    private String updatedBy;
    private Instant updatedDate;
}
