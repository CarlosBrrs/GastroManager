package com.kaiho.gastromanager.domain.report.model.sales;

import com.kaiho.gastromanager.domain.report.model.sales.overview.Filters;
import com.kaiho.gastromanager.domain.report.model.sales.product.ProductSale;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductSalesReport(
        Filters filters,
        List<ProductSale> products
) {
}

