package com.kaiho.gastromanager.application.menu.dto.request;

import lombok.Builder;

@Builder
public record MenuUpdateRequestDto(String name, String description) {
}
