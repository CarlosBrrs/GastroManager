package com.kaiho.gastromanager.infrastructure.invoice.input.rest;

import com.kaiho.gastromanager.application.invoice.dto.response.InvoiceResponseDto;
import com.kaiho.gastromanager.application.invoice.handler.InvoiceHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/invoices")
@AllArgsConstructor
public class InvoiceRestController {

    private final InvoiceHandler invoiceHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<List<InvoiceResponseDto>>> getAllInvoices() {
/*        ApiGenericResponse<List<InvoiceResponseDto>> handlerResponse = invoiceHandler.getAllInvoices();
        return new ResponseEntity<>(handlerResponse, OK);*/
        return null;
    }

    @GetMapping("/{invoiceUuid}")
    public ResponseEntity<ApiGenericResponse<InvoiceResponseDto>> getInvoiceByUUID(@PathVariable UUID invoiceUuid) {
/*        ApiGenericResponse<InvoiceResponseDto> handlerResponse = invoiceHandler.getInvoiceByUUID(invoiceUuid);
        return new ResponseEntity<>(handlerResponse, OK);*/
        return null;
    }


}
