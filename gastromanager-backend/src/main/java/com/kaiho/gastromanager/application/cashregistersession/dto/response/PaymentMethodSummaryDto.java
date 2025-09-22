package com.kaiho.gastromanager.application.cashregistersession.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentMethodSummaryDto(
        String paymentMethodName,
        String paymentMethodDescription,
        BigDecimal totalAmount,
        Integer transactionCount
) {
}
