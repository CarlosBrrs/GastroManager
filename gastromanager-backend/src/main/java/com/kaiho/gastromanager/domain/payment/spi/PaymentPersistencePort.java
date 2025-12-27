package com.kaiho.gastromanager.domain.payment.spi;

import com.kaiho.gastromanager.domain.payment.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentPersistencePort {
    UUID save(Payment payment);

    Optional<Payment> findById(UUID uuid);
}

