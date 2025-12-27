package com.kaiho.gastromanager.infrastructure.invoice.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.invoice.output.jpa.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceEntityRepository extends JpaRepository<InvoiceEntity, UUID> {
/*    @Query("SELECT i FROM InvoiceEntity i WHERE i.order.uuid = :orderUuid")
    List<InvoiceEntity> findAllByOrderUuid(UUID orderUuid);*/
}
