package com.kaiho.gastromanager.domain.orderitem.usecase;

import com.kaiho.gastromanager.domain.order.model.UninvoicedItemDto;
import com.kaiho.gastromanager.domain.orderitem.api.OrderItemServicePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.orderitem.spi.OrderItemPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemUseCase implements OrderItemServicePort {

    private final OrderItemPersistencePort orderItemPersistencePort;

    @Override
    public List<UninvoicedItemDto> getUninvoicedItemsByOrderUuid(UUID orderUuid) {
        // todo validar que exista la orden
//        return orderItemPersistencePort.getUninvoicedItemsByOrderUuid(orderUuid);
        return null;
    }

    public List<OrderItem> getAllOrderItems(List<UUID> orderItemUuids) {
        return orderItemPersistencePort.getOrderItemsByUuids(orderItemUuids);
    }
}
