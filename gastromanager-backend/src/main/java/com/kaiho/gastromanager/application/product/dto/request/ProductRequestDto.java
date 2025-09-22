package com.kaiho.gastromanager.application.product.dto.request;

public record ProductRequestDto(String name, String description, String category, double salePrice, double purchasePrice) {
}
