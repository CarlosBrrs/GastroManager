package com.kaiho.gastromanager.domain.product.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CategoryGroup {
    private final String name;
    private final List<ProductCategoryItem> products;
}
