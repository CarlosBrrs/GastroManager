package com.kaiho.gastromanager.application.report.dto.response.sales;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SummaryDto(
        Integer totalOrders,             // Total de órdenes en el período
        Integer completedOrders,         // Órdenes completadas/servidas
        Integer cancelledOrders,         // Órdenes canceladas
        Double cancellationRate,         // Porcentaje de cancelación
        BigDecimal totalRevenue,             // Revenue de órdenes completadas
        BigDecimal totalPaidWithTips,                // Dinero efectivamente pagado
        Double averageOrderValue       // Ingreso promedio por orden
 /*       Integer activeOrders,        // Órdenes activas (en proceso)
BigDecimal totalTips,                // Total de propinas recibidas
        Integer totalItemsSold,          // Unidades totales vendidas
        Double averageItemsPerOrder,     // Promedio de ítems por orden
        Double averageOrdersPerDay    */          // Promedio de órdenes por día
) {
}