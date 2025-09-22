package com.kaiho.gastromanager.application.cashregistersession.handler;

import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionOpenRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionCloseRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionSummaryResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.UUID;

public interface CashRegisterSessionHandler {

    /**
     * Abre una nueva sesión de caja registradora
     */
    ApiGenericResponse<CashRegisterSessionResponseDto> openSession(CashRegisterSessionOpenRequestDto requestDto);

    /**
     * Cierra una sesión de caja registradora
     */
    ApiGenericResponse<CashRegisterSessionResponseDto> closeSession(UUID sessionUuid, CashRegisterSessionCloseRequestDto requestDto);

    /**
     * Obtiene el resumen detallado de la sesión actual del usuario
     */
    ApiGenericResponse<CashRegisterSessionSummaryResponseDto> getCurrentSessionSummary();
}
