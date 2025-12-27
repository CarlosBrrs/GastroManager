package com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.mapper;

import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.entity.CashRegisterEntity;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.mapper.CashRegisterSessionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashRegisterEntityMapper {

    private final CashRegisterSessionEntityMapper sessionMapper;

    public CashRegister toDomain(CashRegisterEntity entity) {
        if (entity == null) {
            return null;
        }

        // Buscar específicamente una sesión ABIERTA
        CashRegisterSession currentSession = null;
        if (entity.getSessions() != null && !entity.getSessions().isEmpty()) {
            // Buscar la sesión con status OPEN
            CashRegisterSessionEntity openSessionEntity = entity.getSessions().stream()
                                                                .filter(session -> session.getStatus().name().equals("OPEN"))
                                                                .findFirst()
                                                                .orElse(null);

            if (openSessionEntity != null) {
                currentSession = sessionMapper.toDomain(openSessionEntity);
            }
        }

        return CashRegister.builder()
                           .uuid(entity.getUuid())
                           .name(entity.getName())
                           .location(entity.getLocation())
                           .description(entity.getDescription())
                           .deviceId(entity.getDeviceId())
                           .currentSession(currentSession)
                           .createdBy(entity.getCreatedBy())
                           .createdDate(entity.getCreatedDate())
                           .updatedBy(entity.getUpdatedBy())
                           .updatedDate(entity.getUpdatedDate())
                           .build();
    }
}
