package com.kaiho.gastromanager.domain.taxconfig.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TaxConfig {

    private final UUID uuid;
    private TaxType taxType;
    private double taxRate;
    private String description;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;
}
