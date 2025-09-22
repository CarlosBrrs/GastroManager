package com.kaiho.gastromanager.domain.cashregistersession.api;

import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;

import java.util.Optional;
import java.util.UUID;

public interface CashRegisterSessionServicePort {

    CashRegisterSession openSession(CashRegisterSession session);

    /**
     * Encuentra la sesión abierta de un usuario
     */
    Optional<CashRegisterSession> findOpenSessionByUserUuid(UUID userUuid);

    /**
     * Cierra una sesión de caja registradora
     */
    CashRegisterSession closeSession(CashRegisterSession sessionToClose);

    /**
     * Obtiene el resumen detallado de la sesión actual del usuario
     */
    CashRegisterSession getCurrentSessionSummary();
}
