package com.kaiho.gastromanager.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resumen ejecutivo del reporte de ventas
 * Contiene métricas calculadas y breakdowns por diferentes dimensiones
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class SalesReportSummary {
    /**
     * Ingresos totales (ventas + propinas)
     */
    private BigDecimal totalRevenue;

    /**
     * Monto total de propinas recibidas
     */
    private BigDecimal totalTips;

    /**
     * Número de órdenes completadas exitosamente
     */
    private int completedOrders;

    /**
     * Número de órdenes canceladas
     */
    private int cancelledOrders;

    /**
     * Valor promedio por orden (totalRevenue / completedOrders)
     */
    private BigDecimal averageOrderValue;

    /**
     * Desglose de ventas por método de pago con porcentajes
     */
    private List<PaymentMethodSummary> paymentMethodBreakdown;

    /**
     * Desglose de ventas por caja registradora con usuarios asignados
     */
    private List<CashRegisterSummary> cashRegisterBreakdown;
}
