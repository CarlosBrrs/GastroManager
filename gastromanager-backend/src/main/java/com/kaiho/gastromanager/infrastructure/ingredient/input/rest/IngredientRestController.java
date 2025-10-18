package com.kaiho.gastromanager.infrastructure.ingredient.input.rest;

import com.kaiho.gastromanager.application.ingredient.dto.request.AdjustStockRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientUpdateRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientDetailResponseDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientSummaryResponseDto;
import com.kaiho.gastromanager.application.ingredient.handler.IngredientHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.criteria.IngredientSearchCriteria;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
public class IngredientRestController {

    private final IngredientHandler ingredientHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<Page<IngredientSummaryResponseDto>>> getAllIngredients(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        IngredientSearchCriteria criteria = IngredientSearchCriteria.builder()
                                                                    .search(search)
                                                                    .category(category)
                                                                    .sortBy(sort.split(",")[0])
                                                                    .sortDirection(sort.split(",")[1])
                                                                    .page(page)
                                                                    .size(size)
                                                                    .build();
        ApiGenericResponse<Page<IngredientSummaryResponseDto>> handlerResponse = ingredientHandler.getAllIngredients(criteria);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiGenericResponse<List<IngredientSummaryResponseDto>>> getAllIngredientsWithoutPagination() {
        ApiGenericResponse<List<IngredientSummaryResponseDto>> handlerResponse = ingredientHandler.getAllIngredientsWithoutPagination();
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{ingredientUuid}")
    public ResponseEntity<ApiGenericResponse<IngredientDetailResponseDto>> getIngredientById(@PathVariable UUID ingredientUuid) {
        ApiGenericResponse<IngredientDetailResponseDto> handlerResponse = ingredientHandler.getIngredientById(ingredientUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> addIngredient(@RequestBody @Valid IngredientRequestDto ingredientRequestDto) {
        return new ResponseEntity<>(ingredientHandler.addIngredient(ingredientRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{ingredientUuid}")
    public ResponseEntity<ApiGenericResponse<IngredientSummaryResponseDto>> updateIngredient(
            @PathVariable UUID ingredientUuid, @RequestBody IngredientUpdateRequestDto ingredientRequestDto) {
        ApiGenericResponse<IngredientSummaryResponseDto> handlerResponse = ingredientHandler.updateIngredient(ingredientUuid, ingredientRequestDto);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PatchMapping("/{ingredientUuid}/adjust-ingredient-stock")
    public ResponseEntity<ApiGenericResponse<UUID>> adjustIngredientStock(@PathVariable UUID ingredientUuid, @RequestBody @Valid AdjustStockRequestDto adjustStockRequestDto) {
        return new ResponseEntity<>(ingredientHandler.adjustIngredientStock(ingredientUuid, adjustStockRequestDto), HttpStatus.OK);
    }

}
