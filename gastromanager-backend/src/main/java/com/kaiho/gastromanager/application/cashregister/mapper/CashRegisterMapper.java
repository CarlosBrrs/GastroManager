package com.kaiho.gastromanager.application.cashregister.mapper;

import com.kaiho.gastromanager.application.cashregister.dto.response.CashRegisterCurrentSessionResponseDto;
import com.kaiho.gastromanager.application.cashregister.dto.response.CashRegisterResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.mapper.CashRegisterSessionMapper;
import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashRegisterMapper {

    private final CashRegisterSessionMapper sessionMapper;

    public CashRegisterResponseDto toResponse(CashRegister cashRegister) {
        if (cashRegister == null) {
            return null;
        }

        CashRegisterCurrentSessionResponseDto currentSession = null;
        String status = "CLOSED";

        if (cashRegister.getCurrentSession() != null) {
            // Usar el mapper dedicado para mapear la sesión actual
            currentSession = sessionMapper.toCurrentSessionResponse(cashRegister.getCurrentSession());
            status = "OPEN";
        }

        return CashRegisterResponseDto.builder()
                                      .uuid(cashRegister.getUuid())
                                      .name(cashRegister.getName())
                                      .location(cashRegister.getLocation())
                                      .status(status)
                                      .currentSession(currentSession)
                                      .build();
    }
}
