package com.kaiho.gastromanager.domain.report.spi.sales;

import com.kaiho.gastromanager.domain.report.datacontext.ReportDataContext;
import com.kaiho.gastromanager.domain.report.datacontext.sales.OverviewSalesDataContext;
import com.kaiho.gastromanager.domain.report.model.sales.OverviewSalesReport;
import com.kaiho.gastromanager.domain.report.model.sales.overview.Filters;
import com.kaiho.gastromanager.domain.report.model.sales.overview.OverviewSummary;
import com.kaiho.gastromanager.domain.report.usecase.MetricCalculator;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SalesReportEntityAdapter implements SalesReportPersistencePort {

    private final OrderEntityRepository orderEntityRepository;
    private final List<MetricCalculator<OverviewSalesDataContext, ?>> overviewCalculators;

    @Override
    public OverviewSalesReport getOverviewSalesReport(OverviewSalesReportCriteria criteria, UUID restaurantUuid) {

        OverviewSalesDataContext dataContext = OverviewSalesDataContext.create(
                criteria,
                restaurantUuid,
                orderEntityRepository
        );

        // 2. Ejecutar todas las calculadoras
        Map<String, Object> metrics = calculateMetrics(dataContext, overviewCalculators);

        // 3. Construir el reporte
        return buildOverviewReport(dataContext, metrics);
    }

    private <D extends ReportDataContext<?>> Map<String, Object> calculateMetrics(
            D dataContext,
            List<? extends MetricCalculator<D, ?>> calculators) {
        Map<String, Object> results = new HashMap<>();
        for (MetricCalculator<D, ?> calculator : calculators) {
            Object result = calculator.calculate(dataContext);
            results.put(calculator.getMetricName(), result);
        }
        return results;
    }


    private OverviewSalesReport buildOverviewReport(
            OverviewSalesDataContext dataContext,
            Map<String, Object> metrics) {

        OverviewSummary summaryMetrics = (OverviewSummary) metrics.get("overviewSummary");
        Filters filters = Filters.builder()
                                 .reportPeriodStart(dataContext.getCriteria().dateFrom())
                                 .reportPeriodEnd(dataContext.getCriteria().dateTo())
                                 .build();
        return OverviewSalesReport.builder()
                                  .filters(filters)
                                  .summary(summaryMetrics)
                                  .build();
    }
}
