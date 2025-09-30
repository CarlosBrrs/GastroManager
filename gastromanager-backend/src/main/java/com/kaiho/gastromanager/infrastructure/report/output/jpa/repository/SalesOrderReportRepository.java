package com.kaiho.gastromanager.infrastructure.report.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Repositorio para consultas de órdenes en reportes de ventas
 * Utiliza JpaSpecificationExecutor para consultas dinámicas complejas
 */
public interface SalesOrderReportRepository extends JpaRepository<OrderEntity, UUID>, JpaSpecificationExecutor<OrderEntity> {
    // No necesitamos métodos personalizados - las Specifications lo manejan todo
}
