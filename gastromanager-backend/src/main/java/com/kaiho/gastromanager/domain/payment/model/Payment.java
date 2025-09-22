package com.kaiho.gastromanager.domain.payment.model;

import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Payment {
    private UUID uuid;
    private Order order;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private BigDecimal tipAmount;
    private String notes;
    private PaymentState state;
    private String transactionId;
    private Restaurant restaurant;
    private CashRegisterSession cashRegisterSession;
    private Instant createdDate;
    private Instant updatedDate;
    private User createdBy;
    private User updatedBy;
}
