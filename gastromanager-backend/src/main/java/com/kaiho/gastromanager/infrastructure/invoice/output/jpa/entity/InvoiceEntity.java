package com.kaiho.gastromanager.infrastructure.invoice.output.jpa.entity;

import com.kaiho.gastromanager.domain.invoice.model.PaymentStatus;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.entity.InvoiceItemEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InvoiceEntity extends Auditable implements Serializable {
/*
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItemEntity> invoiceItems = new ArrayList<>();*/
    private String customerName;
    private String customerEmail;
    private String customerPhone;

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_uuid", nullable = false)
    private OrderEntity order;*/
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_uuid")
    private RestaurantEntity restaurant;*/

    private BigDecimal amount;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal tipAmount;
    private BigDecimal discount;
    private BigDecimal taxRate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

/*    public void addInvoiceItem(InvoiceItemEntity invoiceItem) {
        invoiceItems.add(invoiceItem);
        invoiceItem.setInvoice(this); // Mantener la relación inversa
    }*/

/*    public void removeInvoiceItem(InvoiceItemEntity invoiceItem) {
        invoiceItems.remove(invoiceItem);
        invoiceItem.setInvoice(null); // Eliminar la relación inversa
    }*/
}