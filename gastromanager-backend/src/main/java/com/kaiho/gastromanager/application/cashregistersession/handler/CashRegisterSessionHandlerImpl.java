package com.kaiho.gastromanager.application.cashregistersession.handler;

import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionCloseRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionOpenRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionSummaryResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.mapper.CashRegisterSessionMapper;
import com.kaiho.gastromanager.domain.cashregistersession.api.CashRegisterSessionServicePort;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Service
@RequiredArgsConstructor
public class CashRegisterSessionHandlerImpl implements CashRegisterSessionHandler {

    private final CashRegisterSessionServicePort cashRegisterSessionServicePort;
    private final CashRegisterSessionMapper cashRegisterSessionMapper;

    @Override
    public ApiGenericResponse<CashRegisterSessionResponseDto> openSession(CashRegisterSessionOpenRequestDto requestDto) {
        CashRegisterSession session = cashRegisterSessionMapper.toDomain(requestDto);
        CashRegisterSession openedSession = cashRegisterSessionServicePort.openSession(session);
        CashRegisterSessionResponseDto response = cashRegisterSessionMapper.toResponse(openedSession);
        return buildSuccessResponse("Session opened successfully", response);
    }

    @Override
    public ApiGenericResponse<CashRegisterSessionResponseDto> closeSession(UUID sessionUuid, CashRegisterSessionCloseRequestDto requestDto) {
        CashRegisterSession sessionToClose = cashRegisterSessionMapper.toDomain(requestDto);
        sessionToClose.setUuid(sessionUuid);
        CashRegisterSession closedSession = cashRegisterSessionServicePort.closeSession(sessionToClose);
        CashRegisterSessionResponseDto response = cashRegisterSessionMapper.toResponse(closedSession);
        return buildSuccessResponse("Session closed successfully", response);
    }

    @Override
    public ApiGenericResponse<CashRegisterSessionSummaryResponseDto> getCurrentSessionSummary() {
        CashRegisterSession sessionWithSummary = cashRegisterSessionServicePort.getCurrentSessionSummary();
        CashRegisterSessionSummaryResponseDto response = cashRegisterSessionMapper.toSummaryResponse(sessionWithSummary);
        return buildSuccessResponse("Current session summary retrieved successfully", response);
    }
}
