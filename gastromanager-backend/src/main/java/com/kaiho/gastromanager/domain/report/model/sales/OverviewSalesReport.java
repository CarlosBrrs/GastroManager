package com.kaiho.gastromanager.domain.report.model.sales;

import com.kaiho.gastromanager.domain.report.model.sales.overview.Filters;
import com.kaiho.gastromanager.domain.report.model.sales.overview.OverviewSummary;
import lombok.Builder;

@Builder
public record OverviewSalesReport(Filters filters, OverviewSummary summary) {

}
