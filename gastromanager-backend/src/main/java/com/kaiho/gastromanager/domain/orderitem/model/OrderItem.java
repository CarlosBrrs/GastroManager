package com.kaiho.gastromanager.domain.orderitem.model;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public final class OrderItem {
    private final UUID uuid;
    private final int quantity;
    private Product product;
    private BigDecimal unitPrice;
    private BigDecimal purchasePrice;
    private String customerNotes;
    private Order order;
    private BigDecimal subtotal;

}
