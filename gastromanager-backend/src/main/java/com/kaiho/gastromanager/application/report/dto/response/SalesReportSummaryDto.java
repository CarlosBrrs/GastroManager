package com.kaiho.gastromanager.application.report.dto.response;

import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
public record SalesReportSummaryDto(
        BigDecimal totalRevenue,
        BigDecimal totalTips,
        int totalOrders,
         Map<PaymentStatus, Integer> orderCountByPaymentStatus, // ✅ Conteo por estado de pago
        Map<PaymentStatus, BigDecimal> revenueByPaymentStatus  // ✅ Ingresos por estado de pago

) {
}
/*        OrderCountPaymentStatus orderCountByPaymentStatus, // ✅ Conteo por estado de pago
        RevenuePaymentStatus revenueByPaymentStatus  // ✅ Ingresos por estado de pago*/