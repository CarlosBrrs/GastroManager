package com.kaiho.gastromanager.infrastructure.product.input.rest;

import com.kaiho.gastromanager.application.product.dto.request.ProductRequestDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductGroupByResponseDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductSummaryResponseDto;
import com.kaiho.gastromanager.application.product.handler.ProductHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductRestController {

    private final ProductHandler productHandler;

    @GetMapping("/search")
    public ResponseEntity<ApiGenericResponse<Page<ProductSummaryResponseDto>>> searchProducts(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                                                              .search(search)
                                                              .sortBy(sort.split(",")[0])
                                                              .sortDirection(sort.split(",")[1])
                                                              .page(page)
                                                              .size(size)
                                                              .build();
        ApiGenericResponse<Page<ProductSummaryResponseDto>> handlerResponse = productHandler.getAllProducts(criteria);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @GetMapping("/group-by")
    public ResponseEntity<ApiGenericResponse<ProductGroupByResponseDto>> getProductsGroupedByCategory(
            @RequestParam(defaultValue = "category") String groupBy,
            @RequestParam(defaultValue = "false") boolean includeEmpty,
            @RequestParam(required = false) Boolean isEnabled
    ) {
        ProductGroupByCriteria criteria = ProductGroupByCriteria.builder()
                                                                .groupBy(groupBy)
                                                                .includeEmpty(includeEmpty)
                                                                .isEnabled(isEnabled)
                                                                .build();
        ApiGenericResponse<ProductGroupByResponseDto> handlerResponse = productHandler.getProductsGroupedByCategory(criteria);
        return new ResponseEntity<>(handlerResponse, OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        return new ResponseEntity<>(productHandler.createProduct(productRequestDto), CREATED);
    }
}
