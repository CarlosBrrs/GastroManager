package com.kaiho.gastromanager.domain.report.datacontext;

import java.util.UUID;

public interface ReportDataContext<C> {
    C getCriteria();
    UUID getRestaurantUuid();
}