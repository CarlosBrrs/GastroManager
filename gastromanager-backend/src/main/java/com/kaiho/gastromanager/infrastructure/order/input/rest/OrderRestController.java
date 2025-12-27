package com.kaiho.gastromanager.infrastructure.order.input.rest;

import com.kaiho.gastromanager.application.invoice.dto.request.InvoiceRequestDto;
import com.kaiho.gastromanager.application.invoice.dto.response.InvoiceResponseDto;
import com.kaiho.gastromanager.application.invoice.handler.InvoiceHandler;
import com.kaiho.gastromanager.application.order.dto.request.ChangeOrderStatusRequestDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderCreateRequestDto;
import com.kaiho.gastromanager.application.order.dto.request.OrderDetailResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderSummaryResponseDto;
import com.kaiho.gastromanager.application.order.dto.response.UninvoicedItemResponseDto;
import com.kaiho.gastromanager.application.order.handler.OrderHandler;
import com.kaiho.gastromanager.application.ticket.handler.TicketHandler;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderRestController {

    private final OrderHandler orderHandler;
    private final TicketHandler ticketHandler;
    private final InvoiceHandler invoiceHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<Page<OrderSummaryResponseDto>>> getAllOrders(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "createdDate,desc") String sort) {
        OrderSearchCriteria criteria = OrderSearchCriteria.builder()
                                                          .search(search)
                                                          .sortBy(sort.split(",")[0])
                                                          .sortDirection(sort.split(",")[1])
                                                          .page(page)
                                                          .size(size)
                                                          .build();
        ApiGenericResponse<Page<OrderSummaryResponseDto>> handlerResponse = orderHandler.getAllOrders(criteria);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @GetMapping("/{orderUuid}")
    public ResponseEntity<ApiGenericResponse<OrderDetailResponseDto>> getOrderByUuid(@PathVariable UUID orderUuid) {
        ApiGenericResponse<OrderDetailResponseDto> handlerResponse = orderHandler.getOrderByUUID(orderUuid);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createOrder(@RequestBody @Valid OrderCreateRequestDto orderRequestDto) {
        return new ResponseEntity<>(orderHandler.createOrder(orderRequestDto), CREATED);
    }

    @PutMapping("/{orderUuid}")
    public ResponseEntity<ApiGenericResponse<OrderResponseDto>> updateOrder(
            @PathVariable UUID orderUuid, @RequestBody OrderCreateRequestDto orderRequestDto) {
        ApiGenericResponse<OrderResponseDto> handlerResponse = orderHandler.updateOrder(orderUuid, orderRequestDto);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @PatchMapping("/{orderUuid}/status")
    public ResponseEntity<ApiGenericResponse<UUID>> changeOrderStatus(
            @PathVariable UUID orderUuid,
            @RequestBody ChangeOrderStatusRequestDto changeOrderStatusRequestDto,
            @AuthenticationPrincipal User user) {

        ApiGenericResponse<UUID> handlerResponse = orderHandler.changeOrderStatus(orderUuid, changeOrderStatusRequestDto, user.getUuid());
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @PostMapping("/{orderUuid}/invoices")
    public ResponseEntity<ApiGenericResponse<UUID>> generateInvoices(@RequestBody InvoiceRequestDto invoiceRequestDto, @PathVariable UUID orderUuid) {
        ApiGenericResponse<UUID> handlerResponse = invoiceHandler.generateInvoice(invoiceRequestDto, orderUuid);
        return new ResponseEntity<>(handlerResponse, CREATED);
    }

    @GetMapping("/{orderUuid}/invoices")
    public ResponseEntity<ApiGenericResponse<List<InvoiceResponseDto>>> getInvoices(@PathVariable UUID orderUuid) {
        ApiGenericResponse<List<InvoiceResponseDto>> handlerResponse = invoiceHandler.getInvoicesByOrderUuid(orderUuid);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @GetMapping("/{orderUuid}/uninvoiced-items")
    public ResponseEntity<ApiGenericResponse<List<UninvoicedItemResponseDto>>> getUninvoicedItems(@PathVariable UUID orderUuid) {
        ApiGenericResponse<List<UninvoicedItemResponseDto>> handlerResponse = orderHandler.getUninvoicedItemsByOrderUuid(orderUuid);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @GetMapping(
            value = "/{orderUuid}/ticket",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> printTicket(@PathVariable UUID orderUuid) {
        byte[] pdfBytes = ticketHandler.generateTicketPdf(orderUuid);

        // Extraer los bytes del PDF desde el response
//        byte[] pdfBytes = handlerResponse.data().pdfBytes();

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION,
                                     "inline; filename=ticket-" + orderUuid + ".pdf")
                             .contentType(MediaType.APPLICATION_PDF)
                             .body(pdfBytes);
    }
}
