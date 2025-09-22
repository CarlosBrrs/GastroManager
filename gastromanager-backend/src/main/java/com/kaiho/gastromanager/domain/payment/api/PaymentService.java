package com.kaiho.gastromanager.domain.payment.api;

import com.kaiho.gastromanager.domain.payment.model.Payment;
import java.util.UUID;

public interface PaymentService {

    UUID createPayment(Payment payment);
}

