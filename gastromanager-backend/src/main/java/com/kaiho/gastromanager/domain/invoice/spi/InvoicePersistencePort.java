package com.kaiho.gastromanager.domain.invoice.spi;

import com.kaiho.gastromanager.domain.invoice.model.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoicePersistencePort {
    UUID generateInvoice(Invoice invoice);

    Invoice getInvoiceByUUID(UUID uuid);

    List<Invoice> getInvoicesByOrderUUID(UUID orderUuid);

}
