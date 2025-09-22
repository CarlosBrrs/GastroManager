package com.kaiho.gastromanager.application.submenu.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SubmenuSummaryResponseDto(UUID uuid,
                                        String name,
                                        String description,
                                        String menuName,
                                        boolean isEnabled) {
}
