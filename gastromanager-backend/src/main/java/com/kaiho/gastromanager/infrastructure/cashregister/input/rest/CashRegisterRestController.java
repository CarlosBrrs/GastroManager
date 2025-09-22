package com.kaiho.gastromanager.infrastructure.cashregister.input.rest;

import com.kaiho.gastromanager.application.cashregister.dto.response.CashRegisterResponseDto;
import com.kaiho.gastromanager.application.cashregister.handler.CashRegisterHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cash-registers")
@RequiredArgsConstructor
public class CashRegisterRestController {

    private final CashRegisterHandler cashRegisterHandler;

    /**
     * Obtiene todas las cajas registradoras del restaurante actual
     *
     * @return Lista de todas las cajas registradoras con sus sesiones actuales si están abiertas
     */
    @GetMapping
    public ResponseEntity<ApiGenericResponse<List<CashRegisterResponseDto>>> getAllCashRegisters() {

        ApiGenericResponse<List<CashRegisterResponseDto>> response =
                cashRegisterHandler.getAllCashRegisters();

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la información de una caja registradora por su UUID
     *
     * @param cashRegisterUuid UUID de la caja registradora
     * @return Información de la caja registradora con su sesión actual si está abierta
     */
    @GetMapping("/{cashRegisterUuid}")
    public ResponseEntity<ApiGenericResponse<CashRegisterResponseDto>> getCashRegister(
            @PathVariable UUID cashRegisterUuid) {

        ApiGenericResponse<CashRegisterResponseDto> response =
                cashRegisterHandler.getCashRegisterById(cashRegisterUuid);

        return ResponseEntity.ok(response);
    }
}
