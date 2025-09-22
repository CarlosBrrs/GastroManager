package com.kaiho.gastromanager.infrastructure.order.output.jpa.entity;

import com.kaiho.gastromanager.domain.order.model.InvoicingStatus;
import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderEntity extends Auditable implements Serializable {

    private String code;
    private BigDecimal totalAmount;

    @Column(name = "total_paid")
    private BigDecimal totalPaid;

    @Column(name = "requires_payment_before_order")
    private Boolean requiresPaymentBeforeOrder;

    private String customerNotes;

    @Column(name = "table_number")
    private String tableNumber;


    @Column(name = "customer_name")
    private String customerName;

    @Enumerated(EnumType.STRING)
    private OperationalStatus operationalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private InvoicingStatus invoicingStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

/*    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceEntity> invoices = new ArrayList<>();*/

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", referencedColumnName = "uuid")
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    public void addPayment(PaymentEntity payment) {
        payments.add(payment);
        payment.setOrder(this);
    }

    public void addOrderItem(OrderItemEntity orderItemEntity) {
        orderItems.add(orderItemEntity);
        orderItemEntity.setOrder(this);
    }

/*    public void addInvoice(InvoiceEntity invoiceEntity) {
        invoices.add(invoiceEntity);
        invoiceEntity.setOrder(this);
    }*/

}
