package com.kaiho.gastromanager.application.ingredient.handler;

import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.application.ingredient.mapper.IngredientMapper;
import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Service
@Transactional
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
}
