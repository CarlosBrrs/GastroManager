package com.kaiho.gastromanager.infrastructure.report.input.rest.sales;

import com.kaiho.gastromanager.application.report.dto.response.sales.OverviewSalesReportResponseDto;
import com.kaiho.gastromanager.application.report.handler.sales.SalesReportHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/reports/sales")
@RequiredArgsConstructor
public class SalesReportRestController {

    private final SalesReportHandler salesReportHandler;

    @GetMapping("/overview")
    public ResponseEntity<ApiGenericResponse<OverviewSalesReportResponseDto>> getOverviewSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dateFrom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dateTo

    ) {
        OverviewSalesReportCriteria criteria = OverviewSalesReportCriteria.builder()
                                                                          .dateFrom(dateFrom)
                                                                          .dateTo(dateTo)
                                                                          .build();

        ApiGenericResponse<OverviewSalesReportResponseDto> response = salesReportHandler.getOverviewSalesReport(criteria);
        return new ResponseEntity<>(response, OK);
    }

}
