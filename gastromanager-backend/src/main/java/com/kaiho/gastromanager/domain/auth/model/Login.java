package com.kaiho.gastromanager.domain.auth.model;

import lombok.Builder;

@Builder
public record Login(String username, String password) {

}
