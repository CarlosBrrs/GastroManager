package com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.adapter;

import com.kaiho.gastromanager.domain.orderitem.exception.OrderItemDoesNotExistException;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.orderitem.spi.OrderItemPersistencePort;
import com.kaiho.gastromanager.infrastructure.invoiceitem.output.jpa.entity.InvoiceItemEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.mapper.OrderItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.repository.OrderItemEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderItemEntityAdapter implements OrderItemPersistencePort {

    private final OrderItemEntityRepository orderItemEntityRepository;
    private final OrderItemEntityMapper orderItemEntityMapper;

    @Override
    public Map<UUID, Integer> getUninvoicedItemsByOrderUuid(UUID orderUuid) {
/*        // Obtener todos los OrderItems de la orden
        List<OrderItemEntity> orderItems = orderItemEntityRepository.findByOrderUuid(orderUuid);

        return orderItems.stream()
                         .collect(Collectors.toMap(
                                 OrderItemEntity::getUuid,
                                 orderItem -> {
                                     int invoiced = orderItem.getInvoiceItems().stream()
                                                             .mapToInt(InvoiceItemEntity::getQuantity)
                                                             .sum();
                                     return orderItem.getQuantity() - invoiced;
                                 }
                         ))
                         .entrySet().stream()
                         .filter(entry -> entry.getValue() > 0)
                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));*/
        return null;
    }

    @Override
    public List<OrderItem> getOrderItemsByUuids(List<UUID> orderItemUuids) {
        List<OrderItemEntity> entities = orderItemEntityRepository.findByUuids(orderItemUuids);

        if (entities.size() != orderItemUuids.size()) {
            throw new OrderItemDoesNotExistException(entities.size(), orderItemUuids.size());
        }

        return entities.stream()
                       .map(orderItemEntityMapper::toDomain)
                       .toList();
    }
}
