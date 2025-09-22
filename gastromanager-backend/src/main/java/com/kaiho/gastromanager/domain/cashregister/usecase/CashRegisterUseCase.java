package com.kaiho.gastromanager.domain.cashregister.usecase;

import com.kaiho.gastromanager.domain.cashregister.api.CashRegisterServicePort;
import com.kaiho.gastromanager.domain.cashregister.exception.CashRegisterNotFoundException;
import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import com.kaiho.gastromanager.domain.cashregister.spi.CashRegisterPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Service
@RequiredArgsConstructor
public class CashRegisterUseCase implements CashRegisterServicePort {

    private final CashRegisterPersistencePort cashRegisterPersistencePort;

    @Override
    public CashRegister getCashRegisterById(UUID cashRegisterUuid) {
        Optional<CashRegister> byIdWithCurrentSession = cashRegisterPersistencePort.findByIdWithCurrentSession(cashRegisterUuid, getCurrentRestaurant());
        return byIdWithCurrentSession
                .orElseThrow(() -> new CashRegisterNotFoundException(cashRegisterUuid));
    }

    @Override
    public List<CashRegister> getAllCashRegisters() {
        return cashRegisterPersistencePort.findAllByRestaurantWithCurrentSession(getCurrentRestaurant());
    }
}
