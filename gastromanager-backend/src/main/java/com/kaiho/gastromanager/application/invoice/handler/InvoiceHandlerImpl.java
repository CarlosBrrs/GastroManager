package com.kaiho.gastromanager.application.invoice.handler;

import com.kaiho.gastromanager.application.invoice.dto.request.InvoiceRequestDto;
import com.kaiho.gastromanager.application.invoice.dto.response.InvoiceResponseDto;
import com.kaiho.gastromanager.application.invoice.mapper.InvoiceMapper;
import com.kaiho.gastromanager.domain.invoice.api.InvoiceServicePort;
import com.kaiho.gastromanager.domain.invoice.model.Invoice;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class InvoiceHandlerImpl implements InvoiceHandler {

    private final InvoiceServicePort invoiceServicePort;
    private final OrderServicePort orderServicePort;
    private final InvoiceMapper invoiceMapper;

    @Override
    public ApiGenericResponse<List<InvoiceResponseDto>> getAllInvoices() {
/*        List<Invoice> productItems = invoiceServicePort.getAllInvoices();
        List<InvoiceResponseDto> invoiceResponseDtoList = productItems.stream().map(invoiceMapper::toResponse).toList();
        return buildSuccessResponse("List of invoices retrieved successfully", invoiceResponseDtoList);*/
        return null;
    }

    @Override
    public ApiGenericResponse<InvoiceResponseDto> getInvoiceByUUID(UUID invoiceUuid) {
        return null;
    }

    @Override
    public ApiGenericResponse<UUID> generateInvoice(InvoiceRequestDto invoiceRequestDto, UUID orderUuid) {
/*        Invoice invoice = invoiceMapper.toDomain(invoiceRequestDto);
        Order order = orderServicePort.getOrderByUUID(orderUuid, getCurrentRestaurant());
        invoice.setOrder(order);
        UUID generatedInvoice = invoiceServicePort.generateInvoice(invoice);

        return buildSuccessResponse("Invoices generated successfully", generatedInvoice);*/
        return null;
    }

    @Override
    public ApiGenericResponse<List<InvoiceResponseDto>> getInvoicesByOrderUuid(UUID orderUuid) {
/*        List<Invoice> invoices = invoiceServicePort.getInvoicesByOrderUuid(orderUuid);
        List<InvoiceResponseDto> invoiceResponseDtoList = invoices.stream().map(invoiceMapper::toResponse).toList();
        return buildSuccessResponse("Invoices for order " + orderUuid + " retrieved successfully", invoiceResponseDtoList);*/
        return null;
    }
}
