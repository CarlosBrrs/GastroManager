package com.kaiho.gastromanager.domain.cashregister.spi;

import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CashRegisterPersistencePort {

    /**
     * Encuentra una caja registradora por su UUID con su sesión actual si existe
     */
    Optional<CashRegister> findByIdWithCurrentSession(UUID cashRegisterUuid, UUID restaurantUuid);

    /**
     * Encuentra todas las cajas registradoras de un restaurante con sus sesiones actuales
     */
    List<CashRegister> findAllByRestaurantWithCurrentSession(UUID restaurantUuid);
}
