package com.kaiho.gastromanager.domain.report.model.sales.product;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductSale(
        String productUuid,
        String productName,
        Integer unitsSold,
        BigDecimal totalSales,
        Double salesPercentage
) {
}

