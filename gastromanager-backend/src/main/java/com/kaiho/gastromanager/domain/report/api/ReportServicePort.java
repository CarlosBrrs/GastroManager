package com.kaiho.gastromanager.domain.report.api;

import com.kaiho.gastromanager.domain.report.model.SalesReport;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;

public interface ReportServicePort {
    SalesReport getSalesReport(SalesReportCriteria criteria);
}
