package com.kaiho.gastromanager.infrastructure.recipe.input.rest;

import com.kaiho.gastromanager.application.recipe.dto.request.RecipeRequestDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeDetailResponseDto;
import com.kaiho.gastromanager.application.recipe.dto.response.RecipeSummaryResponseDto;
import com.kaiho.gastromanager.application.recipe.handler.RecipeHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.recipe.output.jpa.criteria.RecipeSearchCriteria;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeRestController {

    private final RecipeHandler recipeHandler;

    @PostMapping
    public ResponseEntity<ApiGenericResponse<RecipeDetailResponseDto>> createRecipe(@RequestBody @Valid RecipeRequestDto recipeRequestDto) {
        return new ResponseEntity<>(recipeHandler.createRecipe(recipeRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiGenericResponse<Page<RecipeSummaryResponseDto>>> getAllRecipes(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "true", required = false) boolean isEnabled,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        RecipeSearchCriteria criteria = RecipeSearchCriteria.builder()
                                                            .search(search)
                                                            .isEnabled(isEnabled)
                                                            .sortBy(sort.split(",")[0])
                                                            .sortDirection(sort.split(",")[1])
                                                            .page(page)
                                                            .size(size)
                                                            .build();
        ApiGenericResponse<Page<RecipeSummaryResponseDto>> handlerResponse = recipeHandler.getAllRecipes(criteria);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{recipeUuid}")
    public ResponseEntity<ApiGenericResponse<RecipeDetailResponseDto>> getRecipeById(@PathVariable UUID recipeUuid) {
        ApiGenericResponse<RecipeDetailResponseDto> handlerResponse = recipeHandler.getRecipeById(recipeUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }
}
