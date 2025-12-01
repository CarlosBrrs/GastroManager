package com.kaiho.gastromanager.domain.report.model.sales.overview;

import lombok.Builder;

import java.time.Instant;

@Builder
public record Filters(Instant reportPeriodStart, Instant reportPeriodEnd) {
}
