package com.kaiho.gastromanager.domain.cashregistersession.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record CashRegisterSessionSummary(
        UUID sessionUuid,
        UUID cashRegisterUuid,
        String cashRegisterName,
        String cashRegisterLocation,
        Instant openedAt,
        String openedBy,
        BigDecimal openingAmount,
        BigDecimal totalCashPayments,
        BigDecimal expectedCashAmount,
        BigDecimal totalCashMovements,
        List<PaymentMethodSummary> paymentMethodSummaries
) {
}
