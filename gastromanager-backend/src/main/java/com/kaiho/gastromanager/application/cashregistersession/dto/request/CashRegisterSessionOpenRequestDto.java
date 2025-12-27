package com.kaiho.gastromanager.application.cashregistersession.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CashRegisterSessionOpenRequestDto(
        @NotNull(message = "El UUID de la caja registradora es obligatorio")
        UUID cashRegisterUuid,

        @NotNull(message = "El monto inicial es obligatorio")
        @DecimalMin(value = "0.00", message = "El monto inicial debe ser mayor o igual a 0")
        BigDecimal openingAmount,

        String notes
) {
}
