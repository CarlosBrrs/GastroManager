package com.kaiho.gastromanager.application.productitem.mapper;

import com.kaiho.gastromanager.application.productitem.dto.request.ProductItemRequestDto;
import com.kaiho.gastromanager.application.productitem.dto.response.ProductItemResponseDto;
import com.kaiho.gastromanager.application.productitemingredient.dto.request.ProductItemIngredientRequestDto;
import com.kaiho.gastromanager.application.productitemingredient.dto.response.ProductItemIngredientResponseDto;
import com.kaiho.gastromanager.application.productitemingredient.mapper.ProductItemIngredientMapper;
import com.kaiho.gastromanager.domain.productitem.model.Category;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class ProductItemMapper {

    private final ProductItemIngredientMapper productItemIngredientMapper;
    private final RestaurantServicePort restaurantServicePort;

    public ProductItemResponseDto toResponse(ProductItem productItem) {
        if (productItem == null) {
            return null;
        }

        List<ProductItemIngredientResponseDto> productItemIngredientResponseDtoList = productItem.getIngredients().stream()
                .map(productItemIngredientMapper::toResponse).toList();
        return ProductItemResponseDto.builder()
                .uuid(productItem.getUuid())
                .name(productItem.getName())
                .description(productItem.getDescription())
                .price(productItem.getPrice())
                .isEnabled(productItem.isEnabled())
                .category(productItem.getCategory())
                .ingredients(productItemIngredientResponseDtoList)
                .build();
    }

    public ProductItem toDomain(ProductItemRequestDto productItemRequestDto) {
        if (productItemRequestDto == null) {
            return null;
        }

        Restaurant restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());

        Set<ProductItemIngredientRequestDto> productItemIngredientRequestDtoSet = new HashSet<>(productItemRequestDto.ingredients());

        List<ProductItemIngredient> productItemIngredients = productItemIngredientRequestDtoSet.stream()
                .map(productItemIngredientMapper::toDomain)
                .toList();

        return ProductItem.builder()
                .name(productItemRequestDto.name())
                .description(productItemRequestDto.description())
                .price(productItemRequestDto.price())
                .category(Category.valueOf(productItemRequestDto.category()))
                .restaurant(restaurant)
                .isEnabled(true)
                .ingredients(productItemIngredients)
                .build();
    }
}
