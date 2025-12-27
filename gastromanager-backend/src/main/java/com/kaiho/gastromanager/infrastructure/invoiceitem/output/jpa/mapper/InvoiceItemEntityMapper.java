package com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.mapper;

import com.kaiho.gastromanager.domain.invoiceitem.model.InvoiceItem;
import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.entity.InvoiceItemEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper.OrderItemEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceItemEntityMapper {

    private final OrderItemEntityMapper orderItemEntityMapper;

    public InvoiceItem toDomain(InvoiceItemEntity invoiceItemEntity) {
        if (invoiceItemEntity == null) {
            return null;
        }
//        OrderItem orderItem = orderItemEntityMapper.toDomain(invoiceItemEntity.getOrderItem());
        return InvoiceItem.builder()
//                          .orderItem(orderItem)
                          .quantity(invoiceItemEntity.getQuantity())
                          .build();
    }

    public InvoiceItemEntity toEntity(InvoiceItem invoiceItem) {
        if (invoiceItem == null) {
            return null;
        }
        OrderItemEntity orderItemEntity = OrderItemEntity.builder().uuid(invoiceItem.getOrderItem().getUuid()).build();
        return InvoiceItemEntity.builder()
                                .quantity(invoiceItem.getQuantity())
//                                .orderItem(orderItemEntity)
                                .unitPrice(invoiceItem.getBasePrice())
                                .taxRate(invoiceItem.getTaxRate())
                                .taxAmount(invoiceItem.getTaxAmount())
                                .uuid(invoiceItem.getUuid()).build();
    }
}
