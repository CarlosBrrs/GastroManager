package com.kaiho.gastromanager.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Resumen de ventas por sesión individual de caja registradora
 * Representa el desempeño de un usuario específico en una sesión determinada
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashRegisterSessionSummary {
    /** UUID de la sesión de caja registradora */
    private UUID sessionUuid;

    /** UUID del usuario que operó esta sesión */
    private UUID operatorUserUuid;

    /** Nombre completo del usuario operador */
    private String operatorUserName;

    /** Fecha y hora de apertura de la sesión */
    private Instant sessionOpenTime;

    /** Fecha y hora de cierre de la sesión (null si aún está abierta) */
    private Instant sessionCloseTime;

    /** Estado de la sesión (OPENED, CLOSED) */
    private String sessionStatus;

    /** Total de ventas procesadas en esta sesión específica */
    private BigDecimal sessionSales;

    /** Número de órdenes procesadas en esta sesión */
    private int sessionOrderCount;

    /** Número de transacciones realizadas en esta sesión */
    private int sessionTransactionCount;
}
