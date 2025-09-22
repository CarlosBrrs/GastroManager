package com.kaiho.gastromanager.domain.invoiceitem.api;

import com.kaiho.gastromanager.application.invoice.dto.request.InvoiceItemRequestDto;
import com.kaiho.gastromanager.domain.invoiceitem.model.InvoiceItem;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;

import java.util.List;

public interface InvoiceItemServicePort {

    List<InvoiceItem> getInvoiceItemsFromOrderItems(List<OrderItem> orderItems);

    List<InvoiceItem> getInvoiceItemsFromOrderItemUuids(List<InvoiceItemRequestDto> orderItemUuids);
}
