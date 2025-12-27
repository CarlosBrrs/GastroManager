package com.kaiho.gastromanager.application.product.mapper;

import com.kaiho.gastromanager.application.product.dto.response.CategoryGroupDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductCategoryItemDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductGroupByResponseDto;
import com.kaiho.gastromanager.domain.product.model.CategoryGroup;
import com.kaiho.gastromanager.domain.product.model.ProductCategoryItem;
import com.kaiho.gastromanager.domain.product.model.ProductGroupByResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductGroupByMapper {

    public ProductGroupByResponseDto toResponseDto(ProductGroupByResult domainResult) {
        if (domainResult == null) {
            return null;
        }

        List<CategoryGroupDto> categoryDtos = domainResult.getCategories().stream()
                                                          .map(this::toCategoryGroupDto)
                                                          .collect(Collectors.toList());

        return ProductGroupByResponseDto.builder()
                                        .categories(categoryDtos)
                                        .totalProducts(domainResult.getTotalProducts())
                                        .totalCategories(domainResult.getTotalCategories())
                                        .build();
    }

    private CategoryGroupDto toCategoryGroupDto(CategoryGroup categoryGroup) {
        List<ProductCategoryItemDto> productDtos = categoryGroup.getProducts().stream()
                                                                .map(this::toProductCategoryItemDto)
                                                                .collect(Collectors.toList());

        return CategoryGroupDto.builder()
                               .name(categoryGroup.getName())
                               .products(productDtos)
                               .build();
    }

    private ProductCategoryItemDto toProductCategoryItemDto(ProductCategoryItem productItem) {
        return ProductCategoryItemDto.builder()
                                     .id(productItem.getId())
                                     .name(productItem.getName())
                                     .price(productItem.getPrice())
                                     .category(productItem.getCategory())
                                     .description(productItem.getDescription())
                                     .isEnabled(productItem.isEnabled())
                                     .build();
    }
}
