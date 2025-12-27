package com.kaiho.gastromanager.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Modelo principal del reporte de ventas
 * Contiene las métricas generales y los datos detallados del período consultado
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class SalesReport {
    /**
     * Monto total de ventas en el período (suma de todos los pagos completados)
     */
    private BigDecimal totalSales;

    /**
     * Número total de órdenes procesadas en el período
     */
    private int totalOrders;

    /**
     * Fecha y hora de inicio del período del reporte
     */
    private Instant reportPeriodStart;

    /**
     * Fecha y hora de fin del período del reporte
     */
    private Instant reportPeriodEnd;

    /**
     * Lista de detalles granulares agrupados por método de pago
     */
    private List<SalesReportDetail> details;

    /**
     * Resumen ejecutivo con métricas calculadas y breakdowns
     */
    private SalesReportSummary summary;
}
