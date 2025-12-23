package com.kaiho.gastromanager.application.report.dto.response.sales;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductSalesResponseDto(
        String productUuid,
        String productName,
        Integer unitsSold,
        BigDecimal totalSales,
        Double salesPercentage
) {
}

