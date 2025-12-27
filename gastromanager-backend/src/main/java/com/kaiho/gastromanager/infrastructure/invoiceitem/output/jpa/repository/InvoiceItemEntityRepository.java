package com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.entity.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceItemEntityRepository extends JpaRepository<InvoiceItemEntity, UUID> {

/*    @Query("SELECT ii FROM InvoiceItemEntity ii WHERE ii.orderItem.order.uuid = :orderUuid")
    List<InvoiceItemEntity> findByOrderUuid(UUID orderUuid);*/

}
