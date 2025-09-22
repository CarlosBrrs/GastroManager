package com.kaiho.gastromanager.application.submenu.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record SubmenuDetailResponseDto(UUID uuid, String name, String description, UUID menuUuid, String menuName,
                                       boolean isEnabled, String createdBy, Instant createdDate, String updatedBy,
                                       Instant updatedDate) {

}
