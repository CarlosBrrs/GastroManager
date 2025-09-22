package com.kaiho.gastromanager.application.product.handler;

import com.kaiho.gastromanager.application.product.dto.request.ProductRequestDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductGroupByResponseDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductSummaryResponseDto;
import com.kaiho.gastromanager.application.product.mapper.ProductGroupByMapper;
import com.kaiho.gastromanager.application.product.mapper.ProductMapper;
import com.kaiho.gastromanager.domain.product.api.ProductServicePort;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductGroupByResult;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Component
@RequiredArgsConstructor
public class ProductHandlerImpl implements ProductHandler {

    private final ProductMapper productMapper;
    private final ProductGroupByMapper productGroupByMapper;
    private final ProductServicePort productServicePort;

    @Override
    public ApiGenericResponse<UUID> createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toDomain(productRequestDto);
        UUID createdProduct = productServicePort.createProduct(product);
        return buildSuccessResponse("Product created successfully", createdProduct);
    }

    @Override
    public ApiGenericResponse<Page<ProductSummaryResponseDto>> getAllProducts(ProductSearchCriteria criteria) {
        Page<Product> productPage = productServicePort.getAllProducts(criteria);
        Page<ProductSummaryResponseDto> productResponseDtoList = productPage.map(productMapper::toResponseSummary);
        return buildSuccessResponse("List of products retrieved successfully", productResponseDtoList);
    }

    @Override
    public ApiGenericResponse<ProductGroupByResponseDto> getProductsGroupedByCategory(ProductGroupByCriteria criteria) {
        ProductGroupByResult domainResult = productServicePort.getProductsGroupedByCategory(criteria);
        ProductGroupByResponseDto responseDto = productGroupByMapper.toResponseDto(domainResult);
        return buildSuccessResponse("Products grouped by category retrieved successfully", responseDto);
    }
}
