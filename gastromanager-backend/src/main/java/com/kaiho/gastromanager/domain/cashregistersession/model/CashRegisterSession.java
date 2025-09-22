package com.kaiho.gastromanager.domain.cashregistersession.model;

import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashRegisterSession {
    private UUID uuid;
    private CashRegister cashRegister;
    private Restaurant restaurant;
    private User user;
    private Instant openingTime;
    private Instant closingTime;
    private BigDecimal openingAmount;
    private BigDecimal closingAmount;
    private BigDecimal expectedAmount;
    private BigDecimal difference;
    private String notes;
    private CashRegisterSessionStatus status;
    private CashRegisterSessionBalanceStatus balanceStatus;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;

    // Campos adicionales para el resumen (no persistidos, solo para transferencia de datos)
    private BigDecimal totalCashPayments;
    private BigDecimal totalCashMovements;
    private List<PaymentMethodSummary> paymentMethodSummaries;
}
