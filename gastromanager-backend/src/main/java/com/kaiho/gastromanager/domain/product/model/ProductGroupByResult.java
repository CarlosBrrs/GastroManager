package com.kaiho.gastromanager.domain.product.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProductGroupByResult {
    private final List<CategoryGroup> categories;
    private final int totalProducts;
    private final int totalCategories;
}
