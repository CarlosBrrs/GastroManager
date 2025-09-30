package com.kaiho.gastromanager.application.report.handler;

import com.kaiho.gastromanager.application.report.dto.response.SalesReportResponseDto;
import com.kaiho.gastromanager.application.report.mapper.SalesReportMapper;
import com.kaiho.gastromanager.domain.report.api.ReportServicePort;
import com.kaiho.gastromanager.domain.report.model.SalesReport;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Component
@RequiredArgsConstructor
public class ReportHandlerImpl implements ReportHandler {

    private final SalesReportMapper salesReportMapper;
    private final ReportServicePort reportServicePort;

    @Override
    public ApiGenericResponse<SalesReportResponseDto> getSalesReport(SalesReportCriteria criteria) {
        SalesReport salesReport = reportServicePort.getSalesReport(criteria);
        SalesReportResponseDto responseDto = salesReportMapper.toResponseDto(salesReport);
        return buildSuccessResponse("Sales report generated successfully", responseDto);
    }
}
