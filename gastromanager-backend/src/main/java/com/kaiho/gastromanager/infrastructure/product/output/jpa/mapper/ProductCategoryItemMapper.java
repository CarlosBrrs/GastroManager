package com.kaiho.gastromanager.infrastructure.product.output.jpa.mapper;

import com.kaiho.gastromanager.domain.product.model.ProductCategoryItem;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryItemMapper {

    public ProductCategoryItem toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductCategoryItem.builder()
                                  .id(entity.getUuid())
                                  .name(entity.getName())
                                  .price(entity.getSalePrice())
                                  .category(entity.getCategory())
                                  .description(entity.getDescription())
                                  .isEnabled(entity.getIsEnabled())
                                  .build();
    }
}
