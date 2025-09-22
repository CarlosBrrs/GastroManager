package com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.entity.InvoiceItemEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderItemEntity extends Auditable implements Serializable {

    @ManyToOne
    @JoinColumn(name = "order_uuid", nullable = false)
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "product_uuid", nullable = false)
    private ProductEntity product;

/*    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItemEntity> invoiceItems = new ArrayList<>();*/

    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private double discountPercentage;
    private String customerNotes;

/*    public void addInvoiceItem(InvoiceItemEntity invoiceItem) {
        invoiceItems.add(invoiceItem);
        invoiceItem.setOrderItem(this); // Enlace bidireccional
    }

    public void removeInvoiceItem(InvoiceItemEntity invoiceItem) {
        invoiceItems.remove(invoiceItem);
        invoiceItem.setOrderItem(null); // Rompe la relación
    }*/


}
