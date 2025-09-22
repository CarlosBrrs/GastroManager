package com.kaiho.gastromanager.application.recipe.dto.request;

import com.kaiho.gastromanager.application.baseproductingredient.dto.request.RecipeIngredientRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RecipeRequestDto(
        @NotBlank(message = "Debe proporcionar un nombre")
        String name,
        @NotBlank(message = "Debe proporcionar una descripcion")
        String description,
        @NotEmpty(message = "Debe proporcionar al menos un ingrediente")
        @Valid
        List<RecipeIngredientRequestDto> ingredients,
        @Min(value = 1, message = "La porcion minima es 1")
        int yieldPortions,
        BaseRecipeRequestDto baseRecipe

) {
}
