package com.kaiho.gastromanager.domain.report.usecase;

import com.kaiho.gastromanager.domain.report.datacontext.ReportDataContext;

public interface MetricCalculator<D extends ReportDataContext<?>, R> {

    R calculate(D dataContext);
    String getMetricName();

}