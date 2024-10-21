package com.kaiho.gastromanager.application.productitem.mapper;

import com.kaiho.gastromanager.application.productitem.dto.request.ProductItemRequestDto;
import com.kaiho.gastromanager.application.productitem.dto.response.ProductItemResponseDto;
import com.kaiho.gastromanager.application.productitemingredient.dto.response.ProductItemIngredientResponseDto;
import com.kaiho.gastromanager.application.productitemingredient.mapper.ProductItemIngredientMapper;
import com.kaiho.gastromanager.domain.productitem.model.Category;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductItemMapper {

    private final ProductItemIngredientMapper productItemIngredientMapper;

    public ProductItemResponseDto toResponse(ProductItem productItem) {
        if (productItem == null) {
            return null;
        }

        List<ProductItemIngredientResponseDto> productItemIngredientResponseDtoList = productItem.ingredients().stream()
                .map(productItemIngredientMapper::toResponse).toList();
        return ProductItemResponseDto.builder()
                .uuid(productItem.uuid())
                .name(productItem.name())
                .description(productItem.description())
                .price(productItem.price())
                .isEnabled(productItem.isEnabled())
                .category(productItem.category())
                .ingredients(productItemIngredientResponseDtoList)
                .build();
    }

    public ProductItem toDomain(ProductItemRequestDto productItemRequestDto) {
        if (productItemRequestDto == null) {
            return null;
        }
        List<ProductItemIngredient> ingredients = productItemRequestDto.ingredients().stream()
                .map(productItemIngredientMapper::toDomain)
                .toList();
        return ProductItem.builder()
                .name(productItemRequestDto.name())
                .description(productItemRequestDto.description())
                .price(productItemRequestDto.price())
                .category(Category.valueOf(productItemRequestDto.category()))
                .isEnabled(true)
                .ingredients(ingredients)
                .build();
    }
}
