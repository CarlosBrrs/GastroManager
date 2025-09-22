package com.kaiho.gastromanager.application.payment.mapper;

import com.kaiho.gastromanager.application.payment.dto.request.PaymentCreateRequestDto;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.payment.model.Payment;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import org.springframework.stereotype.Component;

import static com.kaiho.gastromanager.domain.payment.model.PaymentState.PENDING;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
public class PaymentMapper {

    public Payment toDomain(PaymentCreateRequestDto paymentCreateRequestDto) {
        if (paymentCreateRequestDto == null) {
            return null;
        }
        Order order = Order.builder()
                           .uuid(paymentCreateRequestDto.orderUuid())
                           .build();
        Restaurant restaurant = Restaurant.builder().uuid(getCurrentRestaurant()).build();
        return Payment.builder()
                      .order(order)
                      .amount(paymentCreateRequestDto.amount())
                      .paymentMethod(paymentCreateRequestDto.paymentMethod())
                      .tipAmount(paymentCreateRequestDto.tipAmount())
                      .notes(paymentCreateRequestDto.notes())
                      .transactionId(paymentCreateRequestDto.transactionId())
                      .state(PENDING)
                      .restaurant(restaurant)
                      .build();
    }
}
