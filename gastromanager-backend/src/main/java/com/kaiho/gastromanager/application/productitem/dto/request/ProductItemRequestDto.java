package com.kaiho.gastromanager.application.productitem.dto.request;

import com.kaiho.gastromanager.application.productitemingredient.dto.request.ProductItemIngredientRequestDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductItemRequestDto(String name,
                                    String description,
                                    double price,
                                    String category,
                                    List<ProductItemIngredientRequestDto> ingredients) {
}
