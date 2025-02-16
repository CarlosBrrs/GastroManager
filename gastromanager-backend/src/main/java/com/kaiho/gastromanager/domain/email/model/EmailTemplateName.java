package com.kaiho.gastromanager.domain.email.model;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    VERIFY_ACCOUNT("verify_account");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
