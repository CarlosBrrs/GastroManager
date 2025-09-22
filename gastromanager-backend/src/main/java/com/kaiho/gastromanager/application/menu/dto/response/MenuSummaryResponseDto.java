package com.kaiho.gastromanager.application.menu.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record MenuSummaryResponseDto(UUID uuid,
                                     String name,
                                     String description,
                                     String createdBy,
                                     Instant createdDate,
                                     String updatedBy,
                                     Instant updatedDate) {
}
