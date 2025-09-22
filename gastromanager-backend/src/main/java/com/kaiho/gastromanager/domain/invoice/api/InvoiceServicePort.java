package com.kaiho.gastromanager.domain.invoice.api;

import com.kaiho.gastromanager.domain.invoice.model.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceServicePort {
    List<Invoice> getAllInvoices();

    UUID generateInvoice(Invoice invoices);

    List<Invoice> getInvoicesByOrderUuid(UUID orderUuid);
}
