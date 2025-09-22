package com.kaiho.gastromanager.infrastructure.payment.output.jpa.adapter;

import com.kaiho.gastromanager.domain.cashregistersession.exception.CashRegisterSessionNotFoundException;
import com.kaiho.gastromanager.domain.order.exception.OrderDoesNotExistException;
import com.kaiho.gastromanager.domain.payment.model.Payment;
import com.kaiho.gastromanager.domain.payment.spi.PaymentPersistencePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.repository.CashRegisterSessionEntityRepository;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.mapper.PaymentEntityMapper;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.repository.PaymentEntityRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class PaymentEntityAdapter implements PaymentPersistencePort {

    private final PaymentEntityRepository paymentEntityRepository;
    private final PaymentEntityMapper paymentEntityMapper;
    private final OrderEntityRepository orderEntityRepository;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final CashRegisterSessionEntityRepository cashRegisterSessionEntityRepository;

    @Override
    public UUID save(Payment payment) {
        PaymentEntity entity = paymentEntityMapper.toEntity(payment);

        UUID orderUuid = payment.getOrder().getUuid();
        OrderEntity orderEntity = orderEntityRepository.findById(orderUuid)
            .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));

        UUID restaurantUuid = payment.getRestaurant().getUuid();
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(restaurantUuid)
            .orElseThrow(() -> new RestaurantDoesNotExistException(restaurantUuid.toString()));

        UUID sessionUuid = payment.getCashRegisterSession().getUuid();
        CashRegisterSessionEntity sessionEntity = cashRegisterSessionEntityRepository.findById(sessionUuid)
            .orElseThrow(() -> new CashRegisterSessionNotFoundException(sessionUuid));

        // Establecer las relaciones bidireccionales
        orderEntity.addPayment(entity);
        restaurantEntity.addPayment(entity);
        sessionEntity.addPayment(entity);

        PaymentEntity saved = paymentEntityRepository.save(entity);
        return saved.getUuid();
    }

    @Override
    public Optional<Payment> findById(UUID uuid) {
        Optional<Payment> payment = paymentEntityRepository.findById(uuid)
                                                           // mapear order, resturant y los createdBy, updatedBy
                                                           .map(paymentEntityMapper::toDomain);
        return payment;
    }

    // Agrega aquí otros métodos del puerto según sea necesario
}
