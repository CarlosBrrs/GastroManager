package com.kaiho.gastromanager.application.product.handler;

import com.kaiho.gastromanager.application.product.dto.request.ProductRequestDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductGroupByResponseDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductSummaryResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductHandler {
    ApiGenericResponse<UUID> createProduct(ProductRequestDto productRequestDto);

    ApiGenericResponse<Page<ProductSummaryResponseDto>> getAllProducts(ProductSearchCriteria criteria);

    ApiGenericResponse<ProductGroupByResponseDto> getProductsGroupedByCategory(ProductGroupByCriteria criteria);
}
