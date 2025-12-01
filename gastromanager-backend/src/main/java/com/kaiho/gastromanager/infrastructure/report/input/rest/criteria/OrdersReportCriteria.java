package com.kaiho.gastromanager.infrastructure.report.input.rest.criteria;

import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record OrdersReportCriteria(
        Instant dateFrom,
        Instant dateTo,
        List<OperationalStatus> operationalStatuses,
        List<PaymentStatus> paymentStatuses
) {
}
