package com.kaiho.gastromanager.domain.orderitem.spi;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderItemPersistencePort {
    Map<UUID, Integer> getUninvoicedItemsByOrderUuid(UUID orderUuid);

    List<OrderItem> getOrderItemsByUuids(List<UUID> orderItemUuids);
}
