package com.kaiho.gastromanager.application.report.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record SalesReportSummaryDto(
        BigDecimal totalRevenue,
        BigDecimal totalTips,
        int completedOrders,
        int cancelledOrders,
        BigDecimal averageOrderValue,
        List<PaymentMethodSummaryDto> paymentMethodBreakdown,
        List<CashRegisterSummaryDto> cashRegisterBreakdown
) {
}
