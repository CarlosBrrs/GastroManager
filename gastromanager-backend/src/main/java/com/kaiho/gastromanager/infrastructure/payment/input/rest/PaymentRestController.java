package com.kaiho.gastromanager.infrastructure.payment.input.rest;

import com.kaiho.gastromanager.application.payment.dto.request.PaymentCreateRequestDto;
import com.kaiho.gastromanager.application.payment.handler.PaymentHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentRestController {
    private final PaymentHandler paymentHandler;

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createPayment(@Valid @RequestBody PaymentCreateRequestDto requestDto) {
        ApiGenericResponse<UUID> response = paymentHandler.createPayment(requestDto);
        return ResponseEntity.ok(response);
    }
}
