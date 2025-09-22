package com.kaiho.gastromanager.application.ingredient.handler;

import com.kaiho.gastromanager.application.ingredient.dto.request.AdjustStockRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientUpdateRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientDetailResponseDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientSummaryResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria.IngredientSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IngredientHandler {
    ApiGenericResponse<Page<IngredientSummaryResponseDto>> getAllIngredients(IngredientSearchCriteria criteria);

    ApiGenericResponse<IngredientDetailResponseDto> getIngredientById(UUID uuid);

    ApiGenericResponse<UUID> addIngredient(IngredientRequestDto ingredientRequestDto);

    ApiGenericResponse<IngredientSummaryResponseDto> updateIngredient(UUID uuid, IngredientUpdateRequestDto ingredientRequestDto);

    ApiGenericResponse<UUID> adjustIngredientStock(UUID ingredientUuid, AdjustStockRequestDto newStock);
}
