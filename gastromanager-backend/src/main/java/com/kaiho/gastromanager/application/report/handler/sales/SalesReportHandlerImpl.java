package com.kaiho.gastromanager.application.report.handler.sales;

import com.kaiho.gastromanager.application.report.dto.response.sales.OverviewSalesReportResponseDto;
import com.kaiho.gastromanager.application.report.dto.response.sales.ProductSalesResponseDto;
import com.kaiho.gastromanager.application.report.mapper.SalesReportMapper;
import com.kaiho.gastromanager.domain.report.api.sales.SalesReportServicePort;
import com.kaiho.gastromanager.domain.report.model.sales.OverviewSalesReport;
import com.kaiho.gastromanager.domain.report.model.sales.ProductSalesReport;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.ProductSalesReportCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Component
@RequiredArgsConstructor
public class SalesReportHandlerImpl implements SalesReportHandler {

    private final SalesReportServicePort salesReportServicePort;
    private final SalesReportMapper salesReportMapper;

    @Override
    public ApiGenericResponse<OverviewSalesReportResponseDto> getOverviewSalesReport(OverviewSalesReportCriteria criteria) {
        OverviewSalesReport overviewSalesReport = salesReportServicePort.getOverviewSalesReport(criteria);
        OverviewSalesReportResponseDto response = salesReportMapper.toResponseDto(overviewSalesReport);
        return buildSuccessResponse("Overview sales report retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<List<ProductSalesResponseDto>> getProductSalesReport(ProductSalesReportCriteria criteria) {
        ProductSalesReport productSalesReport = salesReportServicePort.getProductSalesReport(criteria);
        List<ProductSalesResponseDto> response = salesReportMapper.toProductSalesResponseDto(productSalesReport);
        return buildSuccessResponse("Product sales report retrieved successfully", response);
    }
}
