package com.kaiho.gastromanager.application.invoice.handler;

import com.kaiho.gastromanager.application.invoice.dto.request.InvoiceRequestDto;
import com.kaiho.gastromanager.application.invoice.dto.response.InvoiceResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface InvoiceHandler {
    ApiGenericResponse<List<InvoiceResponseDto>> getAllInvoices();

    ApiGenericResponse<InvoiceResponseDto> getInvoiceByUUID(UUID invoiceUuid);

    ApiGenericResponse<UUID> generateInvoice(InvoiceRequestDto invoiceRequestDto, UUID orderUuid);

    ApiGenericResponse<List<InvoiceResponseDto>> getInvoicesByOrderUuid(UUID orderUuid);
}
