package com.kaiho.gastromanager.domain.cashregister.api;

import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;

import java.util.List;
import java.util.UUID;

public interface CashRegisterServicePort {

    /**
     * Obtiene una caja registradora por su UUID con su sesión actual
     */
    CashRegister getCashRegisterById(UUID cashRegisterUuid);

    /**
     * Obtiene todas las cajas registradoras del restaurante actual con sus sesiones actuales
     */
    List<CashRegister> getAllCashRegisters();
}
