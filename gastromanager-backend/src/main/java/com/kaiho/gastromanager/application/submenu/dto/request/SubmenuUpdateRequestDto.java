package com.kaiho.gastromanager.application.submenu.dto.request;

import lombok.Builder;

@Builder
public record SubmenuUpdateRequestDto(String name, String description) {
}
