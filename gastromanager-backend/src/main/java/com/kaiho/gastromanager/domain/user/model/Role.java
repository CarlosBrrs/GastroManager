package com.kaiho.gastromanager.domain.user.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Role(UUID uuid, RoleType roleType) {

}
