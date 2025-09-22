package com.kaiho.gastromanager.application.cashregister.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record CashRegisterCurrentSessionResponseDto(
    UUID uuid,
    Instant openedAt,
    String openedBy,
    BigDecimal openingAmount
) {
}
