package com.kaiho.gastromanager.application.productitem.dto.response;

import com.kaiho.gastromanager.application.productitemingredient.dto.response.ProductItemIngredientResponseDto;
import com.kaiho.gastromanager.domain.productitem.model.Category;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ProductItemResponseDto(UUID uuid,
                                     String name,
                                     String description,
                                     Category category,
                                     boolean isEnabled,
                                     double price,
                                     List<ProductItemIngredientResponseDto> ingredients) {
}
