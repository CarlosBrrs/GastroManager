package com.kaiho.gastromanager.application.cashregistersession.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record CashRegisterSessionSummaryResponseDto(
        UUID sessionUuid,
        UUID cashRegisterUuid,
        String cashRegisterName,
        String cashRegisterLocation,
        Instant openedAt,
        String openedBy,
        BigDecimal openingAmount,
        BigDecimal totalCashPayments,
        BigDecimal expectedCashAmount,
        BigDecimal totalCashMovements, // Para movimientos futuros
        List<PaymentMethodSummaryDto> paymentMethodSummaries
) {
}
