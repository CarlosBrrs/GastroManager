package com.kaiho.gastromanager.application.payment.handler;

import com.kaiho.gastromanager.application.payment.dto.request.PaymentCreateRequestDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.UUID;

public interface PaymentHandler {
    ApiGenericResponse<UUID> createPayment(PaymentCreateRequestDto requestDto);
}


