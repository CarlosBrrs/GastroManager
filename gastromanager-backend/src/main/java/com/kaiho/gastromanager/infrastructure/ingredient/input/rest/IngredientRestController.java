package com.kaiho.gastromanager.infrastructure.ingredient.input.rest;

import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.application.ingredient.handler.IngredientHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class IngredientRestController {

    private final IngredientHandler ingredientHandler;

    @GetMapping("/api/v1/ingredients")
    public ResponseEntity<ApiGenericResponse<List<IngredientResponseDto>>> getAllIngredients() {
        ApiGenericResponse<List<IngredientResponseDto>> allIngredients = ingredientHandler.getAllIngredients();
        return new ResponseEntity<>(allIngredients, HttpStatus.OK);
    }

    @GetMapping("/api/v1/ingredients/{ingredientId}")
    public ResponseEntity<ApiGenericResponse<IngredientResponseDto>> getIngredientById(@PathVariable UUID ingredientId) {
        ApiGenericResponse<IngredientResponseDto> ingredientById = ingredientHandler.getIngredientById(ingredientId);
        return new ResponseEntity<>(ingredientById, HttpStatus.OK);
    }
}
