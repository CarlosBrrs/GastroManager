package com.kaiho.gastromanager.domain.invoiceitem.model;

import com.kaiho.gastromanager.domain.invoice.model.Invoice;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceItem {
    private final UUID uuid;
    private final OrderItem orderItem;
    private final Invoice invoice;
    private int quantity;
    private BigDecimal basePrice;
    private BigDecimal discount;
    private BigDecimal taxAmount;
    private double taxRate;
}
