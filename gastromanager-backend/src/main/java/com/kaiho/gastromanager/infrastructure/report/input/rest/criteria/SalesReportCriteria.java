package com.kaiho.gastromanager.infrastructure.report.input.rest.criteria;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Criterios de filtrado para el reporte de ventas
 * Permite filtrar por múltiples dimensiones simultáneamente
 */
@Builder
public record SalesReportCriteria(
        // Filtros de tiempo - OBLIGATORIOS
        Instant dateFrom,
        Instant dateTo,

        // Filtros de cajas registradoras - OPCIONALES
        // null = todas las cajas, lista vacía = ninguna, lista con UUIDs = cajas específicas
        List<UUID> cashRegisterUuids,

        // Filtro de estado de sesiones - OPCIONAL
        // OPENED = solo sesiones abiertas, CLOSED = solo cerradas, ALL = todas
        String sessionState, // OPENED|CLOSED|ALL (default: ALL)

        // Filtros de métodos de pago - OPCIONALES
        // null = todos los métodos, lista vacía = ninguno, lista con valores = métodos específicos
        List<String> paymentMethods, // cash, credit_card, debit_card, transfer

        // Filtros de usuarios/cajeros - OPCIONALES
        // null = todos los usuarios, lista vacía = ninguno, lista con UUIDs = usuarios específicos
        List<UUID> assignedUserUuids
) {
}
