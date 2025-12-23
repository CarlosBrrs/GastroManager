package com.kaiho.gastromanager.domain.report.usecase.sales;

import com.kaiho.gastromanager.domain.report.api.sales.SalesReportServicePort;
import com.kaiho.gastromanager.domain.report.model.sales.OverviewSalesReport;
import com.kaiho.gastromanager.domain.report.model.sales.ProductSalesReport;
import com.kaiho.gastromanager.domain.report.spi.sales.SalesReportPersistencePort;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.ProductSalesReportCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Service
@RequiredArgsConstructor
public class SalesReportUseCase implements SalesReportServicePort {

    private final SalesReportPersistencePort salesReportPersistencePort;

    @Override
    public OverviewSalesReport getOverviewSalesReport(OverviewSalesReportCriteria criteria) {
        return salesReportPersistencePort.getOverviewSalesReport(criteria, getCurrentRestaurant());
    }

    @Override
    public ProductSalesReport getProductSalesReport(ProductSalesReportCriteria criteria) {
        return salesReportPersistencePort.getProductSalesReport(criteria, getCurrentRestaurant());
    }
}
