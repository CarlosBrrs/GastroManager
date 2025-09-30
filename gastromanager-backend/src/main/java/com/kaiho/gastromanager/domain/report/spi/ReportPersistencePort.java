package com.kaiho.gastromanager.domain.report.spi;

import com.kaiho.gastromanager.domain.report.model.SalesReport;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;

import java.util.UUID;

public interface ReportPersistencePort {
    SalesReport getSalesReport(SalesReportCriteria criteria, UUID restaurantUuid);
}
