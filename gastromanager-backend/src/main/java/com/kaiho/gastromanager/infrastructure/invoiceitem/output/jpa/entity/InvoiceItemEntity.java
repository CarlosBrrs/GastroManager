package com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class InvoiceItemEntity extends Auditable implements Serializable {

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_uuid", nullable = false)
    private InvoiceEntity invoice;*/
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_uuid", nullable = false)
    private OrderItemEntity orderItem;*/

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 5)
    private double taxRate;

//    @Column(nullable = false, precision = 10, scale = 2)
//    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;
}
