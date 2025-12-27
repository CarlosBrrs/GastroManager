package com.kaiho.gastromanager.infrastructure.payment.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, UUID>, JpaSpecificationExecutor<PaymentEntity> {

}
