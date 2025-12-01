package com.kaiho.gastromanager.application.report.mapper;

import com.kaiho.gastromanager.application.report.dto.response.SalesReportResponseDto;
import com.kaiho.gastromanager.application.report.dto.response.sales.FiltersDto;
import com.kaiho.gastromanager.application.report.dto.response.sales.OverviewSalesReportResponseDto;
import com.kaiho.gastromanager.application.report.dto.response.sales.SummaryDto;
import com.kaiho.gastromanager.domain.report.model.SalesReport;
import com.kaiho.gastromanager.domain.report.model.sales.OverviewSalesReport;
import com.kaiho.gastromanager.domain.report.model.sales.overview.Filters;
import com.kaiho.gastromanager.domain.report.model.sales.overview.OverviewSummary;
import org.springframework.stereotype.Component;

@Component
public class SalesReportMapper {

    public SalesReportResponseDto toResponseDto(SalesReport salesReport) {
/*        if (salesReport == null) {
            return null;
        }

        var filters = SalesReportFiltersResponseDto.builder()
                                                   .reportPeriodStart(salesReport.reportPeriodStart())
                                                   .reportPeriodEnd(salesReport.reportPeriodEnd())
                                                   .build();
        var summary = toSalesSummaryResponseDto(salesReport.salesSummary());
        return SalesReportResponseDto.builder()
                                     .filters(filters)
                                     .salesSummary(summary)
                                     .build();*/
        return null;
    }
/*

    private SalesReportSummaryResponseDto toSalesSummaryResponseDto(SalesSummary salesSummary) {
        if (salesSummary == null) {
            return null;
        }
        return SalesReportSummaryResponseDto.builder()
                                            .totalRevenue(salesSummary.totalRevenue())
                                            .totalTips(salesSummary.totalTips())
                                            .netRevenue(salesSummary.netRevenue())
                                            .totalTransactions(salesSummary.totalTransactions())
                                            .averageTransactionAmount(salesSummary.averageTransactionAmount())
                                            .totalUnitsSold(0)
                                            .uniqueProductsSold(0)
                                            .averageUnitsPerTransaction(BigDecimal.ZERO)
                                            .build();
    }
*/

    public OverviewSalesReportResponseDto toResponseDto(OverviewSalesReport overviewSalesReport) {
        if (overviewSalesReport == null) {
            return null;
        }

        var filtersDto = toFiltersDto(overviewSalesReport.filters());
        var summaryDto = toSummaryDto(overviewSalesReport.summary(), filtersDto.totalDays());

        return OverviewSalesReportResponseDto.builder()
                                             .filters(filtersDto)
                                             .summary(summaryDto)
                                             .build();
    }

    private FiltersDto toFiltersDto(Filters filters) {
        if (filters == null) {
            return null;
        }
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(
                filters.reportPeriodStart().truncatedTo(java.time.temporal.ChronoUnit.DAYS),
                filters.reportPeriodEnd().truncatedTo(java.time.temporal.ChronoUnit.DAYS)
        ) + 1;

        return FiltersDto.builder()
                         .startDate(filters.reportPeriodStart())
                         .endDate(filters.reportPeriodEnd())
                         .totalDays((int) totalDays)
                         .build();
    }

    private SummaryDto toSummaryDto(OverviewSummary summary, Integer totalDays) {
        if (summary == null) {
            return null;
        }

        Integer activeOrders = summary.totalOrders() - summary.completedOrders() - summary.cancelledOrders();
        double averageOrdersPerDay = (totalDays == null || totalDays == 0)
                ? 0.0
                : (double) summary.totalOrders() / totalDays;


        return SummaryDto.builder()
                         .totalOrders(summary.totalOrders())
                         .completedOrders(summary.completedOrders())
                         .cancelledOrders(summary.cancelledOrders())
                         .cancellationRate(summary.cancellationRate())
                         .totalOrderValue(summary.totalOrderValue())
                         .totalRevenue(summary.totalRevenue())
                         .totalTips(summary.totalTips())
                         .totalPaidWithTips(summary.totalPaidWithTips())
                         .pendingAmount(summary.pendingAmount())
                         .collectionRate(summary.collectionRate())
                         .averageOrderValue(summary.averageOrderValue())
                         /*.activeOrders(activeOrders)
                         .totalItemsSold(0) // totalItemsSold no está disponible en el modelo
                         .averageItemsPerOrder(0.0) // averageItemsPerOrder no está disponible
                         .averageOrdersPerDay(averageOrdersPerDay)*/
                         .build();
    }
}

