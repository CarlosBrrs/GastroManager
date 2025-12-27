package com.kaiho.gastromanager.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Resumen de ventas agrupado por caja registradora
 * Incluye información de todas las sesiones operadas en esa caja durante el período
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashRegisterSummary {
    /**
     * UUID único de la caja registradora
     */
    private UUID cashRegisterUuid;

    /**
     * Nombre descriptivo de la caja registradora
     */
    private String cashRegisterName;

    /**
     * Total de ventas procesadas en esta caja durante el período
     */
    private BigDecimal totalSales;

    /**
     * Número total de órdenes procesadas en esta caja
     */
    private int orderCount;

    /**
     * Total de transacciones realizadas en esta caja
     */
    private int transactionCount;

    /**
     * Lista de resúmenes por sesión - cada sesión tiene su usuario operador
     */
    private List<CashRegisterSessionSummary> sessionSummaries;
}
