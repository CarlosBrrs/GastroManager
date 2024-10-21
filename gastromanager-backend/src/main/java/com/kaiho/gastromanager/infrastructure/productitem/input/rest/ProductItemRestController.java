package com.kaiho.gastromanager.infrastructure.productitem.input.rest;

import com.kaiho.gastromanager.application.productitem.dto.request.ProductItemRequestDto;
import com.kaiho.gastromanager.application.productitem.dto.response.ProductItemResponseDto;
import com.kaiho.gastromanager.application.productitem.handler.ProductItemHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.endpoint.base-url}/product-items")
@AllArgsConstructor
public class ProductItemRestController {

    private final ProductItemHandler productItemHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<List<ProductItemResponseDto>>> getAllProductItems() {
        ApiGenericResponse<List<ProductItemResponseDto>> handlerResponse = productItemHandler.getAllProductItems();
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{productItemUuid}")
    public ResponseEntity<ApiGenericResponse<ProductItemResponseDto>> getProductItemByUUID(@PathVariable UUID productItemUuid) {
        ApiGenericResponse<ProductItemResponseDto> handlerResponse = productItemHandler.getProductItemByUUID(productItemUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> addProductItem(@RequestBody ProductItemRequestDto productItemRequestDto) {
        return new ResponseEntity<>(productItemHandler.addProductItem(productItemRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{productItemUuid}")
    public ResponseEntity<ApiGenericResponse<ProductItemResponseDto>> updateProductItem(
            @PathVariable UUID productItemUuid, @RequestBody ProductItemRequestDto productItemRequestDto) {
        ApiGenericResponse<ProductItemResponseDto> handlerResponse = productItemHandler.updateProductItem(productItemUuid, productItemRequestDto);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }
}
