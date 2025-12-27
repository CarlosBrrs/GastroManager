package com.kaiho.gastromanager.domain.cashregistersession.spi;

import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.domain.cashregistersession.model.PaymentMethodSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CashRegisterSessionPersistencePort {

    /**
     * Guarda una nueva sesión de caja registradora
     */
    CashRegisterSession save(CashRegisterSession session);

    /**
     * Actualiza una sesión existente
     */
    CashRegisterSession update(CashRegisterSession session);

    /**
     * Encuentra una sesión por su UUID
     */
    Optional<CashRegisterSession> findById(UUID sessionUuid);


    /**
     * Verifica si existe una sesión abierta para una caja registradora
     */
    boolean existsOpenSessionByCashRegisterUuid(UUID cashRegisterUuid);

    /**
     * Verifica si una caja registradora pertenece a un restaurante específico
     */
    boolean existsCashRegisterByUuidAndRestaurant(UUID cashRegisterUuid, UUID restaurantUuid);

    /**
     * Verifica si un usuario tiene alguna sesión abierta
     */
    boolean existsOpenSessionByUserUuid(UUID userUuid);

    /**
     * Encuentra la sesión abierta de un usuario
     */
    Optional<CashRegisterSession> findOpenSessionByUserUuid(UUID userUuid);

    /**
     * Calcula el total de pagos en efectivo de una sesión
     */
    BigDecimal calculateCashPaymentsTotalBySessionUuid(UUID sessionUuid);


    /**
     * Obtiene el resumen de pagos agrupados por método de pago para una sesión
     */
    List<PaymentMethodSummary> getPaymentMethodSummaryBySessionUuid(UUID sessionUuid);

    /**
     * Calcula el total de movimientos de caja para una sesión (para implementación futura)
     */
    BigDecimal calculateCashMovementsTotalBySessionUuid(UUID sessionUuid);
}