package com.kaiho.gastromanager.domain.report.usecase.sales;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
class ProductMetrics {
    private final String productUuid;
    private final String productName;
    private int unitsSold;
    private BigDecimal totalSales;

    public ProductMetrics(String productUuid, String productName) {
        this.productUuid = productUuid;
        this.productName = productName;
        this.unitsSold = 0;
        this.totalSales = BigDecimal.ZERO;
    }

    public void addSale(int quantity, BigDecimal sales) {
        this.unitsSold += quantity;
        this.totalSales = this.totalSales.add(sales);
    }

}

