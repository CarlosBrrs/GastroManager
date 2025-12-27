package com.kaiho.gastromanager.application.ingredient.handler;

import com.kaiho.gastromanager.application.ingredient.dto.request.AdjustStockRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientUpdateRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientDetailResponseDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientSummaryResponseDto;
import com.kaiho.gastromanager.application.ingredient.mapper.IngredientMapper;
import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria.IngredientSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@RequiredArgsConstructor
@Component
public class IngredientHandlerImpl implements IngredientHandler {

    private final IngredientServicePort ingredientServicePort;
    private final IngredientMapper ingredientMapper;
    private final RestaurantServicePort restaurantServicePort;

    @Override
    public ApiGenericResponse<Page<IngredientSummaryResponseDto>> getAllIngredients(IngredientSearchCriteria criteria) {
        Page<Ingredient> ingredientPage = ingredientServicePort.getAllIngredients(criteria);

        Page<IngredientSummaryResponseDto> ingredientResponseDtoList = ingredientPage.map(ingredientMapper::toResponseSummary);

        return buildSuccessResponse("List of ingredients retrieved successfully", ingredientResponseDtoList);
    }

    @Override
    public ApiGenericResponse<List<IngredientSummaryResponseDto>> getAllIngredientsWithoutPagination() {
        List<Ingredient> ingredients = ingredientServicePort.getAllIngredientsWithoutPagination();

        List<IngredientSummaryResponseDto> ingredientResponseList = ingredients.stream()
                                                                               .map(ingredientMapper::toResponseSummary)
                                                                               .toList();

        return buildSuccessResponse("All ingredients retrieved successfully", ingredientResponseList);
    }

    @Override
    public ApiGenericResponse<IngredientDetailResponseDto> getIngredientById(UUID uuid) {
        Ingredient ingredientById = ingredientServicePort.getIngredientById(uuid);
        IngredientDetailResponseDto response = ingredientMapper.toResponseDetail(ingredientById);
        return buildSuccessResponse("Ingredient retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<UUID> addIngredient(IngredientRequestDto ingredientRequestDto) {
        Ingredient ingredient = ingredientMapper.toDomain(ingredientRequestDto);
        ingredient.setRestaurant(restaurantServicePort.getRestaurantById(getCurrentRestaurant()));
        UUID ingredientUuid = ingredientServicePort.addIngredient(ingredient);
        return buildSuccessResponse("Ingredient added successfully", ingredientUuid);
    }

    @Override
    public ApiGenericResponse<IngredientSummaryResponseDto> updateIngredient(UUID uuid, IngredientUpdateRequestDto ingredientRequestDto) {
        Ingredient ingredient = ingredientMapper.toDomain(ingredientRequestDto);
        ingredient.setRestaurant(restaurantServicePort.getRestaurantById(getCurrentRestaurant()));
        Ingredient updateIngredient = ingredientServicePort.updateIngredient(uuid, ingredient);
        IngredientSummaryResponseDto response = ingredientMapper.toResponseSummary(updateIngredient);
        return buildSuccessResponse("Ingredient updated successfully", response);
    }

    @Override
    public ApiGenericResponse<UUID> adjustIngredientStock(UUID ingredientUuid, AdjustStockRequestDto adjustStockRequestDto) {
        UUID returnedUuid = ingredientServicePort.adjustIngredientStock(ingredientUuid, adjustStockRequestDto.newStock(), adjustStockRequestDto.reason());
        return buildSuccessResponse("Ingredient stock adjusted successfully", returnedUuid);
    }
}
