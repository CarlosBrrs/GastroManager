package com.kaiho.gastromanager.domain.product.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class ProductCategoryItem {
    private final UUID id;
    private final String name;
    private final BigDecimal price;
    private final String category;
    private final String description;
    private final boolean isEnabled;
}
