package com.kaiho.gastromanager.domain.report.model.sales.overview;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OverviewSummary(
        Integer totalOrders,
        Integer completedOrders,
        Integer cancelledOrders,
        Double cancellationRate,

        // Métricas basadas en órdenes (valor teórico de ventas)
        BigDecimal totalOrderValue,      // Suma de order.totalAmount - lo que se debería cobrar

        // Métricas basadas en pagos (dinero real cobrado)
        BigDecimal totalRevenue,         // Suma de payment.amount (sin propina) - dinero cobrado neto
        BigDecimal totalTips,            // Suma de payment.tipAmount - propinas cobradas
        BigDecimal totalPaidWithTips,// Suma de payment.amount + tipAmount - dinero total cobrado (revenue + tips)

        // Métricas derivadas
        BigDecimal pendingAmount,        // totalOrderValue - totalRevenue - deuda pendiente (sin contar propinas)
        Double collectionRate,           // (totalRevenue / totalOrderValue) * 100 - % de cobro efectivo

        Double averageOrderValue
) {
}
