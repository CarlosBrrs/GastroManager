package com.kaiho.gastromanager.infrastructure.invoice.output.jpa.mapper;

import com.kaiho.gastromanager.domain.invoice.model.Invoice;
import com.kaiho.gastromanager.infrastructure.invoice.output.jpa.entity.InvoiceEntity;
import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.mapper.InvoiceItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper.OrderEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InvoiceEntityMapper {

    private final OrderEntityMapper orderEntityMapper;
    private final InvoiceItemEntityMapper invoiceItemEntityMapper;

    public InvoiceEntity toEntity(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
//        List<InvoiceItemEntity> invoiceItemEntityList = invoice.getInvoiceItems().stream()
//                .map(invoiceItemEntityMapper::toEntity)
//                .collect(Collectors.toCollection(ArrayList::new));
        return InvoiceEntity.builder()
                            .uuid(invoice.getUuid())
                            .customerName(invoice.getCustomerName())
                            .customerEmail(invoice.getCustomerEmail())
                            .customerPhone(invoice.getCustomerPhone())
//                            .invoiceItems(new ArrayList<>())
                            .subtotal(invoice.getSubtotal())
                            .taxAmount(invoice.getTaxAmount())
                            .tipAmount(invoice.getTipAmount())
                            .amount(invoice.getAmount())
                            .discount(invoice.getDiscount())
                            .taxRate(BigDecimal.valueOf(invoice.getTaxRate()))
                            .paymentStatus(invoice.getStatus())
                            .amount(invoice.getAmount()).build();
    }

    public Invoice toDomain(InvoiceEntity invoiceEntity) {
        if (invoiceEntity == null) {
            return null;
        }
//        Order order = orderEntityMapper.toDomain(invoiceEntity.getOrder());
/*
        List<InvoiceItem> invoiceItems = invoiceEntity.getInvoiceItems().stream()
                                                      .map(invoiceItemEntityMapper::toDomain)
                                                      .toList();
*/
        return Invoice.builder()
                      .uuid(invoiceEntity.getUuid())
//                      .order(order)
//                      .invoiceItems(invoiceItems)
                      .amount(invoiceEntity.getAmount())
                      .status(invoiceEntity.getPaymentStatus())
                      .createdBy(invoiceEntity.getCreatedBy())
                      .createdDate(invoiceEntity.getCreatedDate())
                      .build();
    }
}
