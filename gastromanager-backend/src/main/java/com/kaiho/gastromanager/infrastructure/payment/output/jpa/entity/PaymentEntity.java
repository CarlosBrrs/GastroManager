package com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity;

import com.kaiho.gastromanager.domain.payment.model.PaymentMethod;
import com.kaiho.gastromanager.domain.payment.model.PaymentState;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PaymentEntity extends Auditable implements Serializable {

    @ManyToOne
    @JoinColumn(name = "order_uuid", referencedColumnName = "uuid", nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "tip_amount")
    private BigDecimal tipAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentState state;

    private String notes;

    @Column(name = "transaction_id")
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", referencedColumnName = "uuid", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "cash_register_session_uuid", referencedColumnName = "uuid", nullable = false)
    private CashRegisterSessionEntity cashRegisterSession;
}

