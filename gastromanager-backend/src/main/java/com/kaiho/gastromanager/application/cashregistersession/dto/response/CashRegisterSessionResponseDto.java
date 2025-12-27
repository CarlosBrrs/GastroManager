package com.kaiho.gastromanager.application.cashregistersession.dto.response;

import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSessionStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record CashRegisterSessionResponseDto(
        UUID uuid,
        UUID cashRegisterUuid,
        String cashRegisterName,
        String cashRegisterLocation,
        Instant openingTime,
        Instant closingTime,
        BigDecimal openingAmount,
        BigDecimal closingAmount,
        CashRegisterSessionStatus status,
        String createdBy,
        Instant createdDate
) {
}
