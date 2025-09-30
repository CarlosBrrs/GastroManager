package com.kaiho.gastromanager.infrastructure.report.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Repositorio para consultas de pagos en reportes de ventas
 * Utiliza JpaSpecificationExecutor para consultas dinámicas complejas
 */
public interface SalesReportRepository extends JpaRepository<PaymentEntity, UUID>, JpaSpecificationExecutor<PaymentEntity> {
    // No necesitamos métodos personalizados - las Specifications lo manejan todo
}
