package com.kaiho.gastromanager.infrastructure.cashregistersession.input.rest;

import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionCloseRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionOpenRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionSummaryResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.handler.CashRegisterSessionHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cash-register-sessions")
@RequiredArgsConstructor
public class CashRegisterSessionRestController {

    private final CashRegisterSessionHandler cashRegisterSessionHandler;

    @PostMapping("/open")
    public ResponseEntity<ApiGenericResponse<CashRegisterSessionResponseDto>> openSession(
            @Valid @RequestBody CashRegisterSessionOpenRequestDto requestDto) {

        ApiGenericResponse<CashRegisterSessionResponseDto> handlerResponse = cashRegisterSessionHandler.openSession(requestDto);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @PutMapping("/{sessionUuid}/close")
    public ResponseEntity<ApiGenericResponse<CashRegisterSessionResponseDto>> closeSession(
            @PathVariable UUID sessionUuid,
            @Valid @RequestBody CashRegisterSessionCloseRequestDto requestDto) {

        ApiGenericResponse<CashRegisterSessionResponseDto> handlerResponse = cashRegisterSessionHandler.closeSession(sessionUuid, requestDto);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @GetMapping("/current-summary")
    public ResponseEntity<ApiGenericResponse<CashRegisterSessionSummaryResponseDto>> getCurrentSessionSummary() {
        ApiGenericResponse<CashRegisterSessionSummaryResponseDto> handlerResponse = cashRegisterSessionHandler.getCurrentSessionSummary();
        return new ResponseEntity<>(handlerResponse, OK);
    }
}