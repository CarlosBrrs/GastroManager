package com.kaiho.gastromanager.application.payment.handler;

import com.kaiho.gastromanager.application.payment.dto.request.PaymentCreateRequestDto;
import com.kaiho.gastromanager.application.payment.mapper.PaymentMapper;
import com.kaiho.gastromanager.domain.payment.api.PaymentService;
import com.kaiho.gastromanager.domain.payment.model.Payment;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Service
@RequiredArgsConstructor
public class PaymentHandlerImpl implements PaymentHandler {

    private final PaymentMapper paymentMapper;
    private final PaymentService paymentService;

    @Override
    public ApiGenericResponse<UUID> createPayment(PaymentCreateRequestDto paymentCreateRequestDto) {
        Payment payment = paymentMapper.toDomain(paymentCreateRequestDto);
        UUID paymentUuid = paymentService.createPayment(payment);
        return buildSuccessResponse("Pago creado exitosamente", paymentUuid);
    }
}


