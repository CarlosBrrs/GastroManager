package com.kaiho.gastromanager.domain.cashregister.model;

import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CashRegister {
    private UUID uuid;
    private String name;
    private String location;
    private String description;
    private String deviceId;
    private CashRegisterSession currentSession; // Sesión activa si existe
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;
}
