package com.kaiho.gastromanager.application.invoice.dto.response;

import com.kaiho.gastromanager.domain.invoice.model.PaymentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record InvoiceResponseDto(UUID uuid, double amount, PaymentStatus status, String createdBy,
                                 Instant createdDate) {
}
