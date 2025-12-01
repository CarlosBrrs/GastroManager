package com.kaiho.gastromanager.application.report.handler.sales;

import com.kaiho.gastromanager.application.report.dto.response.sales.OverviewSalesReportResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;

public interface SalesReportHandler {
    ApiGenericResponse<OverviewSalesReportResponseDto> getOverviewSalesReport(OverviewSalesReportCriteria criteria);
}
