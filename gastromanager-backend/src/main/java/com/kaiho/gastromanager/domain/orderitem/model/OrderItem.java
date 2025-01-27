package com.kaiho.gastromanager.domain.orderitem.model;

import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public final class OrderItem {
    private final ProductItem productItem;
    private final int quantity;
    private double unitPrice;

}
