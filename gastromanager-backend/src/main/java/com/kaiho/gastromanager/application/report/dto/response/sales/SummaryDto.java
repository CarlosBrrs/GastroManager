package com.kaiho.gastromanager.application.report.dto.response.sales;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SummaryDto(
        Integer totalOrders,             // Total de órdenes en el período
        Integer completedOrders,         // Órdenes completadas/servidas
        Integer cancelledOrders,         // Órdenes canceladas
        Double cancellationRate,         // Porcentaje de cancelación

        // Métricas basadas en órdenes (valor teórico)
        BigDecimal totalOrderValue,      // Valor total de órdenes (lo que se debería cobrar)

        // Métricas basadas en pagos (dinero real cobrado)
        BigDecimal totalRevenue,         // Dinero cobrado neto (sin propinas)
        BigDecimal totalTips,            // Total de propinas cobradas
        BigDecimal totalPaidWithTips,    // Dinero total cobrado (revenue + tips)

        // Métricas derivadas
        BigDecimal pendingAmount,        // Deuda pendiente por cobrar
        Double collectionRate,           // Porcentaje de cobro efectivo
        Double averageOrderValue         // Valor promedio por orden
) {
}