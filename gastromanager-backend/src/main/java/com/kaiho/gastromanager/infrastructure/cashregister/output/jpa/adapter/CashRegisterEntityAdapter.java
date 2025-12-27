package com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.adapter;

import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import com.kaiho.gastromanager.domain.cashregister.spi.CashRegisterPersistencePort;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.entity.CashRegisterEntity;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.mapper.CashRegisterEntityMapper;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.repository.CashRegisterEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CashRegisterEntityAdapter implements CashRegisterPersistencePort {

    private final CashRegisterEntityRepository cashRegisterRepository;
    private final CashRegisterEntityMapper cashRegisterMapper;

    @Override
    public Optional<CashRegister> findByIdWithCurrentSession(UUID cashRegisterUuid, UUID restaurantUuid) {
        Optional<CashRegisterEntity> entityOpt = cashRegisterRepository.findByUuidAndRestaurantUuidWithSessions(cashRegisterUuid, restaurantUuid);
        return entityOpt.map(cashRegisterMapper::toDomain);
    }

    @Override
    public List<CashRegister> findAllByRestaurantWithCurrentSession(UUID restaurantUuid) {
        // Usar una sola query que obtiene todas las cajas con solo sus sesiones abiertas
        List<CashRegisterEntity> cashRegistersWithOpenSessions = cashRegisterRepository.findAllByRestaurantUuidWithOpenSessions(restaurantUuid);

        // Obtener todas las cajas del restaurante para asegurar que no se pierda ninguna
        List<CashRegisterEntity> allCashRegisters = cashRegisterRepository.findAllByRestaurantUuid(restaurantUuid);

        // Crear un mapa de las cajas con sesiones abiertas para referencia rápida
        Map<UUID, CashRegisterEntity> openSessionsMap = cashRegistersWithOpenSessions.stream()
                                                                                     .collect(Collectors.toMap(
                                                                                             CashRegisterEntity::getUuid,
                                                                                             entity -> entity
                                                                                     ));

        // Mapear todas las cajas, enriqueciendo con sesiones abiertas si existen
        return allCashRegisters.stream()
                               .map(cashRegister -> {
                                   // Si la caja tiene una sesión abierta, usar esa entidad enriquecida
                                   CashRegisterEntity entityToMap = openSessionsMap.getOrDefault(
                                           cashRegister.getUuid(),
                                           cashRegister
                                   );
                                   return cashRegisterMapper.toDomain(entityToMap);
                               })
                               .toList();
    }
}
