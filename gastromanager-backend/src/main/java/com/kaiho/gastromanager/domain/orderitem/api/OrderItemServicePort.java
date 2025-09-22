package com.kaiho.gastromanager.domain.orderitem.api;

import com.kaiho.gastromanager.domain.order.model.UninvoicedItemDto;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemServicePort {
    List<UninvoicedItemDto> getUninvoicedItemsByOrderUuid(UUID orderUuid);

    List<OrderItem> getAllOrderItems(List<UUID> orderItemUuids);
}
