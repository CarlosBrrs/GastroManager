package com.kaiho.gastromanager.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Detalle granular de ventas agrupado por método de pago
 * Proporciona información consolidada de cada método de pago utilizado
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class SalesReportDetail {
    /** Nombre del método de pago (cash, credit_card, debit_card, transfer) */
    private String paymentMethodName;

    /** Monto total cobrado con este método de pago (suma de todas las transacciones) */
    private BigDecimal totalAmount;

    /** Cantidad total de órdenes pagadas con este método */
    private int orderCount;

    /** Cantidad total de transacciones realizadas con este método */
    private int transactionCount;

    /** Monto promedio por transacción con este método de pago */
    private BigDecimal averageTransactionAmount;
}
