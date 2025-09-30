package com.kaiho.gastromanager.domain.report.usecase;

import com.kaiho.gastromanager.domain.report.api.ReportServicePort;
import com.kaiho.gastromanager.domain.report.model.SalesReport;
import com.kaiho.gastromanager.domain.report.spi.ReportPersistencePort;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Service
@RequiredArgsConstructor
public class ReportUseCase implements ReportServicePort {

    private final ReportPersistencePort reportPersistencePort;

    @Override
    public SalesReport getSalesReport(SalesReportCriteria criteria) {
        return reportPersistencePort.getSalesReport(criteria, getCurrentRestaurant());
    }
}
