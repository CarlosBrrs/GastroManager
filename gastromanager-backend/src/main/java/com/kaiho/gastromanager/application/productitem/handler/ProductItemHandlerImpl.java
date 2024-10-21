package com.kaiho.gastromanager.application.productitem.handler;

import com.kaiho.gastromanager.application.productitem.dto.request.ProductItemRequestDto;
import com.kaiho.gastromanager.application.productitem.dto.response.ProductItemResponseDto;
import com.kaiho.gastromanager.application.productitem.mapper.ProductItemMapper;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class ProductItemHandlerImpl implements ProductItemHandler {

    private final ProductItemServicePort productItemServicePort;
    private final ProductItemMapper productItemMapper;

    @Override
    public ApiGenericResponse<List<ProductItemResponseDto>> getAllProductItems() {
        List<ProductItem> productItems = productItemServicePort.getAllProductItems();
        List<ProductItemResponseDto> productItemsResponseDto = productItems.stream().map(productItemMapper::toResponse).toList();
        return buildSuccessResponse("List of product items retrieved successfully", productItemsResponseDto);
    }

    @Override
    public ApiGenericResponse<ProductItemResponseDto> getProductItemByUUID(UUID uuid) {
        ProductItem productItemByUUID = productItemServicePort.getProductItemByUUID(uuid);
        ProductItemResponseDto response = productItemMapper.toResponse(productItemByUUID);
        return buildSuccessResponse("Product item retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<UUID> addProductItem(ProductItemRequestDto productItemRequestDto) {
        ProductItem productItem = productItemMapper.toDomain(productItemRequestDto);
        UUID productItemUuid = productItemServicePort.addProductItem(productItem);
        return buildSuccessResponse("Product item added successfully", productItemUuid);
    }

    @Override
    public ApiGenericResponse<ProductItemResponseDto> updateProductItem(UUID uuid, ProductItemRequestDto productItemRequestDto) {
        ProductItem productItem = productItemMapper.toDomain(productItemRequestDto);
        ProductItem updateProductItem = productItemServicePort.updateProductItem(uuid, productItem);
        ProductItemResponseDto response = productItemMapper.toResponse(updateProductItem);
        return buildSuccessResponse("Product item updated successfully", response);
    }
}
