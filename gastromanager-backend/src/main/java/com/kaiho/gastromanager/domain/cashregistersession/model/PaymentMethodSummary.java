package com.kaiho.gastromanager.domain.cashregistersession.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentMethodSummary(
        String paymentMethodName,
        String paymentMethodDescription,
        BigDecimal totalAmount,
        Integer transactionCount
) {
}
