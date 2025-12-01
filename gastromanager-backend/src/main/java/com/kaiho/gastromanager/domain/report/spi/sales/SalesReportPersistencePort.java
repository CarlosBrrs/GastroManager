package com.kaiho.gastromanager.domain.report.spi.sales;

import com.kaiho.gastromanager.domain.report.model.sales.OverviewSalesReport;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;

import java.util.UUID;

public interface SalesReportPersistencePort {
    OverviewSalesReport getOverviewSalesReport(OverviewSalesReportCriteria criteria, UUID currentRestaurant);
}
