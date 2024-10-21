package com.kaiho.gastromanager.application.productitem.handler;

import com.kaiho.gastromanager.application.productitem.dto.request.ProductItemRequestDto;
import com.kaiho.gastromanager.application.productitem.dto.response.ProductItemResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.List;
import java.util.UUID;

public interface ProductItemHandler {
    ApiGenericResponse<List<ProductItemResponseDto>> getAllProductItems();

    ApiGenericResponse<ProductItemResponseDto> getProductItemByUUID(UUID productItemUuid);

    ApiGenericResponse<UUID> addProductItem(ProductItemRequestDto productItemRequestDto);

    ApiGenericResponse<ProductItemResponseDto> updateProductItem(UUID productItemUuid, ProductItemRequestDto productItemRequestDto);
}
