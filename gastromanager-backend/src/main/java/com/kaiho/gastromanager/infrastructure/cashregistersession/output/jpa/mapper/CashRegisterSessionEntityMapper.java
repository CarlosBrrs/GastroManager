package com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.mapper;

import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashRegisterSessionEntityMapper {

    private final RestaurantEntityMapper restaurantMapper;
    private final UserEntityMapper userMapper;

    public CashRegisterSessionEntity toEntity(CashRegisterSession domain) {
        if (domain == null) {
            return null;
        }

        return CashRegisterSessionEntity.builder()
                                        .uuid(domain.getUuid())
                                        .openingTime(domain.getOpeningTime())
                                        .closingTime(domain.getClosingTime())
                                        .openingAmount(domain.getOpeningAmount())
                                        .closingAmount(domain.getClosingAmount())
                                        .status(domain.getStatus())
                                        .notes(domain.getNotes())
                                        .build();
    }

    public CashRegisterSession toDomain(CashRegisterSessionEntity entity) {
        if (entity == null) {
            return null;
        }

        return CashRegisterSession.builder()
                                  .uuid(entity.getUuid())
                                  .cashRegister(entity.getCashRegister() != null ?
                                          CashRegister.builder()
                                                      .uuid(entity.getCashRegister().getUuid())
                                                      .name(entity.getCashRegister().getName())
                                                      .location(entity.getCashRegister().getLocation())
                                                      .description(entity.getCashRegister().getDescription())
                                                      .deviceId(entity.getCashRegister().getDeviceId())
                                                      .build() : null)
                                  .restaurant(entity.getCashRegister() != null && entity.getCashRegister().getRestaurant() != null ?
                                          restaurantMapper.toDomain(entity.getCashRegister().getRestaurant()) : null)
                                  .user(entity.getUser() != null ? userMapper.toDomain(entity.getUser()) : null)
                                  .openingTime(entity.getOpeningTime())
                                  .closingTime(entity.getClosingTime())
                                  .openingAmount(entity.getOpeningAmount())
                                  .closingAmount(entity.getClosingAmount())
                                  .expectedAmount(entity.getExpectedAmount())
                                  .difference(entity.getDifference())
                                  .balanceStatus(entity.getBalanceStatus())
                                  .status(entity.getStatus())
                                  .notes(entity.getNotes())
                                  .createdBy(entity.getCreatedBy())
                                  .createdDate(entity.getCreatedDate())
                                  .updatedBy(entity.getUpdatedBy())
                                  .updatedDate(entity.getUpdatedDate())
                                  .build();
    }
}
