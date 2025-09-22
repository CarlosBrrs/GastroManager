package com.kaiho.gastromanager.infrastructure.invoice.output.jpa.adapter;

import com.kaiho.gastromanager.domain.invoice.model.Invoice;
import com.kaiho.gastromanager.domain.invoice.spi.InvoicePersistencePort;
import com.kaiho.gastromanager.domain.invoiceitem.model.InvoiceItem;
import com.kaiho.gastromanager.infrastructure.invoice.output.jpa.entity.InvoiceEntity;
import com.kaiho.gastromanager.infrastructure.invoice.output.jpa.mapper.InvoiceEntityMapper;
import com.kaiho.gastromanager.infrastructure.invoice.output.jpa.repository.InvoiceEntityRepository;
import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.entity.InvoiceItemEntity;
import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.mapper.InvoiceItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InvoiceEntityAdapter implements InvoicePersistencePort {

    private final InvoiceEntityMapper invoiceEntityMapper;
    private final OrderEntityRepository orderEntityRepository;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final InvoiceEntityRepository invoiceEntityRepository;
    private final InvoiceItemEntityMapper invoiceItemEntityMapper;

    @Override
    public UUID generateInvoice(Invoice invoices) {
        InvoiceEntity invoiceEntity = invoiceEntityMapper.toEntity(invoices);
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(invoices.getRestaurant().getUuid()).orElseThrow(() -> new IllegalArgumentException("Restaurant not found for the invoice"));
//        restaurantEntity.addInvoice(invoiceEntity);
        OrderEntity orderEntity = orderEntityRepository.findById(invoices.getOrder().getUuid()).orElseThrow(() -> new IllegalArgumentException("Order not found for invoice"));
//        orderEntity.addInvoice(invoiceEntity);

        for (InvoiceItem invoiceItem : invoices.getInvoiceItems()) {
            InvoiceItemEntity invoiceItemEntity = invoiceItemEntityMapper.toEntity(invoiceItem);
            OrderItemEntity orderItemEntity = orderEntity.getOrderItems().stream()
                                                         .filter(orItEntity -> orItEntity.getUuid().equals(invoiceItem.getOrderItem().getUuid()))
                                                         .findFirst()
                                                         .orElseThrow(() -> new IllegalArgumentException("OrderItem not found for the invoiceItem"));

            // Asegúrate de que las colecciones se modifiquen correctamente
//            orderItemEntity.addInvoiceItem(invoiceItemEntity);
//            invoiceEntity.addInvoiceItem(invoiceItemEntity);
        }


        return invoiceEntityRepository.save(invoiceEntity).getUuid();
    }

    @Override
    public Invoice getInvoiceByUUID(UUID uuid) {
        return null;
    }

    @Override
    public List<Invoice> getInvoicesByOrderUUID(UUID orderUuid) {
/*        List<InvoiceEntity> allByOrderUuid = invoiceEntityRepository.findAllByOrderUuid(orderUuid);
        return allByOrderUuid.stream()
                             .map(invoiceEntityMapper::toDomain).toList();*/
        return null;
    }


}
