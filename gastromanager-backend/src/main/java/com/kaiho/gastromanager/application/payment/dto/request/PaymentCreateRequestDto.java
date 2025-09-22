package com.kaiho.gastromanager.application.payment.dto.request;

import com.kaiho.gastromanager.domain.payment.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PaymentCreateRequestDto(
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    BigDecimal amount,

    @NotNull(message = "El método de pago es obligatorio")
    PaymentMethod paymentMethod,

    @DecimalMin(value = "0.00", message = "La propina no puede ser negativa")
    BigDecimal tipAmount,

    String notes,

    @NotNull(message = "El UUID de la orden es obligatorio")
    UUID orderUuid,

    String transactionId
) {
}
