package com.kaiho.gastromanager.application.ingredient.handler;

import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IngredientHandler {
    ApiGenericResponse<List<IngredientResponseDto>> getAllIngredients();

    ApiGenericResponse<IngredientResponseDto> getIngredientById(UUID uuid);

    ApiGenericResponse<UUID> addIngredient(IngredientRequestDto ingredientRequestDto);
}
