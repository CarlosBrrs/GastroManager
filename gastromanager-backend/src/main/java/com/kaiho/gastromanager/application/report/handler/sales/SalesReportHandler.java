package com.kaiho.gastromanager.application.report.handler.sales;

import com.kaiho.gastromanager.application.report.dto.response.sales.OverviewSalesReportResponseDto;
import com.kaiho.gastromanager.application.report.dto.response.sales.ProductSalesResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.ProductSalesReportCriteria;

import java.util.List;

public interface SalesReportHandler {
    ApiGenericResponse<OverviewSalesReportResponseDto> getOverviewSalesReport(OverviewSalesReportCriteria criteria);

    ApiGenericResponse<List<ProductSalesResponseDto>> getProductSalesReport(ProductSalesReportCriteria criteria);
}
