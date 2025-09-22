package com.kaiho.gastromanager.application.cashregister.handler.impl;

import com.kaiho.gastromanager.application.cashregister.dto.response.CashRegisterResponseDto;
import com.kaiho.gastromanager.application.cashregister.handler.CashRegisterHandler;
import com.kaiho.gastromanager.application.cashregister.mapper.CashRegisterMapper;
import com.kaiho.gastromanager.domain.cashregister.api.CashRegisterServicePort;
import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Service
@RequiredArgsConstructor
public class CashRegisterHandlerImpl implements CashRegisterHandler {

    private final CashRegisterServicePort cashRegisterServicePort;
    private final CashRegisterMapper cashRegisterMapper;

    @Override
    public ApiGenericResponse<CashRegisterResponseDto> getCashRegisterById(UUID cashRegisterUuid) {
        CashRegister cashRegister = cashRegisterServicePort.getCashRegisterById(cashRegisterUuid);
        CashRegisterResponseDto response = cashRegisterMapper.toResponse(cashRegister);
        return buildSuccessResponse("Caja registradora obtenida exitosamente", response);
    }

    @Override
    public ApiGenericResponse<List<CashRegisterResponseDto>> getAllCashRegisters() {
        List<CashRegister> cashRegisters = cashRegisterServicePort.getAllCashRegisters();
        List<CashRegisterResponseDto> response = cashRegisters.stream()
                .map(cashRegisterMapper::toResponse)
                .toList();
        return buildSuccessResponse("Cajas registradoras obtenidas exitosamente", response);
    }
}
