package com.kaiho.gastromanager.application.cashregistersession.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CashRegisterSessionCloseRequestDto(
        @NotNull(message = "Closing amount is required")
        @DecimalMin(value = "0.0", message = "Closing amount must be positive")
        BigDecimal closingAmount,

        String notes
) {
}
