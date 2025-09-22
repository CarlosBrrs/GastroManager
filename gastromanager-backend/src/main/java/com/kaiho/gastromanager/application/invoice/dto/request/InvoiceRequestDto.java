package com.kaiho.gastromanager.application.invoice.dto.request;

import java.util.List;

public record InvoiceRequestDto(
        String customerName, String customerPhone, String customerEmail, List<InvoiceItemRequestDto> invoiceItems,
        double tipAmount
) {
}
