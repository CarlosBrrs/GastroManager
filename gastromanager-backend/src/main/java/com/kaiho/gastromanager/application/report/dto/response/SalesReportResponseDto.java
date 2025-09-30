package com.kaiho.gastromanager.application.report.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record SalesReportResponseDto(
        BigDecimal totalSales,
        int totalOrders,
        Instant reportPeriodStart,
        Instant reportPeriodEnd,
        SalesReportSummaryDto summary,
        List<SalesReportDetailDto> details
) {
}
