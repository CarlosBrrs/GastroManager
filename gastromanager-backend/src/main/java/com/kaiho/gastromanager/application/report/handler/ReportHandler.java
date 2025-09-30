package com.kaiho.gastromanager.application.report.handler;

import com.kaiho.gastromanager.application.report.dto.response.SalesReportResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;

public interface ReportHandler {
    ApiGenericResponse<SalesReportResponseDto> getSalesReport(SalesReportCriteria criteria);
}
