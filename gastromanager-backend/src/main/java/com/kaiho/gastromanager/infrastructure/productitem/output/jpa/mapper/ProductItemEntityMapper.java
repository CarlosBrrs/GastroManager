package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.mapper;

import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.mapper.ProductItemIngredientEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductItemEntityMapper {

    private final ProductItemIngredientEntityMapper productItemIngredientEntityMapper;

    public ProductItem toDomain(ProductItemEntity productItemEntity) {
        if (productItemEntity == null) {
            return null;
        }

        List<ProductItemIngredient> ingredients = productItemEntity.getIngredients().stream()
                .map(productItemIngredientEntityMapper::toDomain)
                .toList();

        return ProductItem.builder()
                .uuid(productItemEntity.getUuid())
                .name(productItemEntity.getName())
                .description(productItemEntity.getDescription())
                .category(productItemEntity.getCategory())
                .price(productItemEntity.getPrice())
                .isEnabled(productItemEntity.isEnabled())
                .ingredients(ingredients)
                .createdBy(productItemEntity.getCreatedBy())
                .createdDate(productItemEntity.getCreatedDate())
                .updatedBy(productItemEntity.getUpdatedBy())
                .updatedDate(productItemEntity.getUpdatedDate())
                .build();
    }

    public ProductItemEntity toEntity(ProductItem productItem) {
        if (productItem == null) {
            return null;
        }
        return ProductItemEntity.builder()
                .uuid(productItem.uuid())
                .name(productItem.name())
                .description(productItem.description())
                .category(productItem.category())
                .price(productItem.price())
                .ingredients(new ArrayList<>())
                .isEnabled(productItem.isEnabled())
                .createdBy(productItem.createdBy())
                .createdDate(productItem.createdDate())
                .build();

    }
}
