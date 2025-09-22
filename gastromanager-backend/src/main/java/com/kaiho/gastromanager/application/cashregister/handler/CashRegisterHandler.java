package com.kaiho.gastromanager.application.cashregister.handler;

import com.kaiho.gastromanager.application.cashregister.dto.response.CashRegisterResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface CashRegisterHandler {

    /**
     * Obtiene una caja registradora por su UUID
     */
    ApiGenericResponse<CashRegisterResponseDto> getCashRegisterById(UUID cashRegisterUuid);

    /**
     * Obtiene todas las cajas registradoras del restaurante actual
     */
    ApiGenericResponse<List<CashRegisterResponseDto>> getAllCashRegisters();
}