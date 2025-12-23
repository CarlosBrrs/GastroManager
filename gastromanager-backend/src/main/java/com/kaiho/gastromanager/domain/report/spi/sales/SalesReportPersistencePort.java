package com.kaiho.gastromanager.domain.report.spi.sales;

import com.kaiho.gastromanager.domain.report.model.sales.OverviewSalesReport;
import com.kaiho.gastromanager.domain.report.model.sales.ProductSalesReport;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.ProductSalesReportCriteria;

import java.util.UUID;

public interface SalesReportPersistencePort {
    OverviewSalesReport getOverviewSalesReport(OverviewSalesReportCriteria criteria, UUID currentRestaurant);

    ProductSalesReport getProductSalesReport(ProductSalesReportCriteria criteria, UUID currentRestaurant);
}
