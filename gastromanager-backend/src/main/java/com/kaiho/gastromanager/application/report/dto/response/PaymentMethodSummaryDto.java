package com.kaiho.gastromanager.application.report.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentMethodSummaryDto(
        String methodName,
        BigDecimal totalAmount,
        int transactionCount,
        BigDecimal percentage
) {
}
