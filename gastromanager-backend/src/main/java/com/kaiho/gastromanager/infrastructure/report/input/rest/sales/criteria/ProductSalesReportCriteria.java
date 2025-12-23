package com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ProductSalesReportCriteria(
        Instant dateFrom,
        Instant dateTo
) {
}

