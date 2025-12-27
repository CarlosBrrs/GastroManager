package com.kaiho.gastromanager.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Resumen de ventas agrupado por método de pago
 * Incluye totales, cantidad de transacciones y porcentaje del total
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class PaymentMethodSummary {
    /**
     * Nombre del método de pago (efectivo, tarjeta de crédito, etc.)
     */
    private String methodName;

    /**
     * Monto total procesado con este método de pago
     */
    private BigDecimal totalAmount;

    /**
     * Número de transacciones realizadas con este método
     */
    private int transactionCount;

    /**
     * Porcentaje que representa este método del total de ventas
     */
    private BigDecimal percentage;
}
