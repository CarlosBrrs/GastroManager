package com.kaiho.gastromanager.application.ingredient.handler;

import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.application.ingredient.mapper.IngredientMapper;
import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class IngredientHandlerImpl implements IngredientHandler {

    private final IngredientServicePort ingredientServicePort;
    private final IngredientMapper ingredientMapper;

    @Override
    public ApiGenericResponse<List<IngredientResponseDto>> getAllIngredients() {
        List<Ingredient> ingredientList = ingredientServicePort.getAllIngredients();

        var ingredientResponseDtoList = ingredientList.stream().map(ingredientMapper::toResponse).toList();

        return buildSuccessResponse("List of ingredients retrieved successfully", ingredientResponseDtoList);
    }

    @Override
    public ApiGenericResponse<IngredientResponseDto> getIngredientById(UUID uuid) {
        Ingredient ingredientById = ingredientServicePort.getIngredientById(uuid);
        IngredientResponseDto response = ingredientMapper.toResponse(ingredientById);
        return buildSuccessResponse("Ingredient retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<UUID> addIngredient(IngredientRequestDto ingredientRequestDto) {
        Ingredient ingredient = ingredientMapper.toDomain(ingredientRequestDto);
        UUID ingredientUuid = ingredientServicePort.addIngredient(ingredient);
        return buildSuccessResponse("Ingredient added successfully", ingredientUuid);
    }

    @Override
    public ApiGenericResponse<IngredientResponseDto> updateIngredient(UUID uuid, IngredientRequestDto ingredientRequestDto) {
        Ingredient ingredient = ingredientMapper.toDomain(ingredientRequestDto);
        Ingredient updateIngredient = ingredientServicePort.updateIngredient(uuid, ingredient);
        IngredientResponseDto response = ingredientMapper.toResponse(updateIngredient);
        return buildSuccessResponse("Ingredient updated successfully", response);
    }
}
