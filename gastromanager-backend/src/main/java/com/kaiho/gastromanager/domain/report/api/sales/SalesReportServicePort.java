package com.kaiho.gastromanager.domain.report.api.sales;

import com.kaiho.gastromanager.domain.report.model.sales.OverviewSalesReport;
import com.kaiho.gastromanager.domain.report.model.sales.ProductSalesReport;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.ProductSalesReportCriteria;

public interface SalesReportServicePort {

    OverviewSalesReport getOverviewSalesReport(OverviewSalesReportCriteria criteria);

    ProductSalesReport getProductSalesReport(ProductSalesReportCriteria criteria);

}
