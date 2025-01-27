package com.kaiho.gastromanager.infrastructure.inventorymovement.input.rest;

import com.kaiho.gastromanager.application.inventorymovement.dto.response.InventoryMovementResponseDto;
import com.kaiho.gastromanager.application.inventorymovement.handler.InventoryMovementHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.endpoint.base-url}/inventory-movements")
@RequiredArgsConstructor
public class InventoryMovementRestController {

    private final InventoryMovementHandler inventoryMovementHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<List<InventoryMovementResponseDto>>> getAllInventoryMovements() {
        ApiGenericResponse<List<InventoryMovementResponseDto>> allInventoryMovementResponse = inventoryMovementHandler.getAllInventoryMovements();
        return new ResponseEntity<>(allInventoryMovementResponse, HttpStatus.OK);
    }

    @GetMapping("/{inventoryMovementUuid}")
    public ResponseEntity<ApiGenericResponse<InventoryMovementResponseDto>> getInventoryMovementByUuid(@PathVariable UUID inventoryMovementUuid) {
        ApiGenericResponse<InventoryMovementResponseDto> inventoryMovementResponse = inventoryMovementHandler.getMovementByUuid(inventoryMovementUuid);
        return new ResponseEntity<>(inventoryMovementResponse, HttpStatus.OK);
    }


}
