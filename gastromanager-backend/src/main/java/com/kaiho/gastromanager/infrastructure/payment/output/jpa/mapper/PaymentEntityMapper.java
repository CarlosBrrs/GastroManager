package com.kaiho.gastromanager.infrastructure.payment.output.jpa.mapper;

import com.kaiho.gastromanager.domain.payment.model.Payment;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.mapper.CashRegisterSessionEntityMapper;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.repository.CashRegisterSessionEntityRepository;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.mapper.OrderEntityMapper;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEntityMapper {

    private final OrderEntityMapper orderMapper;
    private final RestaurantEntityMapper restaurantMapper;
    private final CashRegisterSessionEntityMapper cashRegisterSessionMapper;

    public PaymentEntity toEntity(Payment domain) {
        if (domain == null) {
            return null;
        }

        return PaymentEntity.builder()
                .uuid(domain.getUuid())
                .amount(domain.getAmount())
                .tipAmount(domain.getTipAmount())
                .paymentMethod(domain.getPaymentMethod())
                .state(domain.getState())
                .notes(domain.getNotes())
                .transactionId(domain.getTransactionId())
                .build();
    }

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        return Payment.builder()
                .uuid(entity.getUuid())
                .order(entity.getOrder() != null ? orderMapper.toDomain(entity.getOrder()) : null)
                .amount(entity.getAmount())
                .tipAmount(entity.getTipAmount())
                .paymentMethod(entity.getPaymentMethod())
                .state(entity.getState())
                .notes(entity.getNotes())
                .transactionId(entity.getTransactionId())
                .restaurant(entity.getRestaurant() != null ? restaurantMapper.toDomain(entity.getRestaurant()) : null)
                .cashRegisterSession(entity.getCashRegisterSession() != null ? cashRegisterSessionMapper.toDomain(entity.getCashRegisterSession()) : null)
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }
}
