package com.kaiho.gastromanager.application.report.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SalesReportDetailDto(
        String paymentMethodName,
        BigDecimal totalAmount,
        int orderCount,
        int transactionCount,
        BigDecimal averageTransactionAmount
) {
}
