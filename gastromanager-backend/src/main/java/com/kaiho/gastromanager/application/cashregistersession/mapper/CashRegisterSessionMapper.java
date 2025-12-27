package com.kaiho.gastromanager.application.cashregistersession.mapper;

import com.kaiho.gastromanager.application.cashregister.dto.response.CashRegisterCurrentSessionResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionCloseRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.request.CashRegisterSessionOpenRequestDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.CashRegisterSessionSummaryResponseDto;
import com.kaiho.gastromanager.application.cashregistersession.dto.response.PaymentMethodSummaryDto;
import com.kaiho.gastromanager.domain.cashregister.model.CashRegister;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;


@Component
public class CashRegisterSessionMapper {

    public CashRegisterSession toDomain(CashRegisterSessionOpenRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        CashRegister cashRegister = CashRegister.builder()
                                                .uuid(requestDto.cashRegisterUuid())
                                                .build();

        Restaurant restaurant = Restaurant.builder()
                                          .uuid(getCurrentRestaurant())
                                          .build();

        return CashRegisterSession.builder()
                                  .cashRegister(cashRegister)
                                  .restaurant(restaurant)
                                  .openingAmount(requestDto.openingAmount())
                                  .notes(requestDto.notes())
                                  .build();
    }

    public CashRegisterSession toDomain(CashRegisterSessionCloseRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return CashRegisterSession.builder()

                                  .closingAmount(requestDto.closingAmount())
                                  .notes(requestDto.notes())
                                  .build();
    }

    public CashRegisterSessionResponseDto toResponse(CashRegisterSession session) {
        if (session == null) {
            return null;
        }

        return CashRegisterSessionResponseDto.builder()
                                             .uuid(session.getUuid())
                                             .cashRegisterUuid(session.getCashRegister().getUuid())
                                             .cashRegisterName(session.getCashRegister().getName())
                                             .cashRegisterLocation(session.getCashRegister().getLocation())
                                             .openingTime(session.getOpeningTime())
                                             .closingTime(session.getClosingTime())
                                             .openingAmount(session.getOpeningAmount())
                                             .closingAmount(session.getClosingAmount())
                                             .status(session.getStatus())
                                             .createdBy(session.getCreatedBy())
                                             .createdDate(session.getCreatedDate())
                                             .build();
    }

    public CashRegisterCurrentSessionResponseDto toCurrentSessionResponse(CashRegisterSession session) {
        if (session == null) {
            return null;
        }

        return CashRegisterCurrentSessionResponseDto.builder()
                                                    .uuid(session.getUuid())
                                                    .openedAt(session.getOpeningTime())
                                                    .openedBy(session.getCreatedBy())
                                                    .openingAmount(session.getOpeningAmount())
                                                    .build();
    }

    public CashRegisterSessionSummaryResponseDto toSummaryResponse(CashRegisterSession session) {
        if (session == null) {
            return null;
        }

        // Mapear los resúmenes de métodos de pago
        List<PaymentMethodSummaryDto> paymentMethodDtos = session.getPaymentMethodSummaries() != null ?
                session.getPaymentMethodSummaries().stream()
                       .map(paymentSummary -> PaymentMethodSummaryDto.builder()
                                                                     .paymentMethodName(paymentSummary.paymentMethodName())
                                                                     .paymentMethodDescription(paymentSummary.paymentMethodDescription())
                                                                     .totalAmount(paymentSummary.totalAmount())
                                                                     .transactionCount(paymentSummary.transactionCount())
                                                                     .build())
                       .toList() : List.of();

        return CashRegisterSessionSummaryResponseDto.builder()
                                                    .sessionUuid(session.getUuid())
                                                    .cashRegisterUuid(session.getCashRegister().getUuid())
                                                    .cashRegisterName(session.getCashRegister().getName())
                                                    .cashRegisterLocation(session.getCashRegister().getLocation())
                                                    .openedAt(session.getOpeningTime())
                                                    .openedBy(session.getCreatedBy())
                                                    .openingAmount(session.getOpeningAmount())
                                                    .totalCashPayments(session.getTotalCashPayments())
                                                    .expectedCashAmount(session.getExpectedAmount())
                                                    .totalCashMovements(session.getTotalCashMovements())
                                                    .paymentMethodSummaries(paymentMethodDtos)
                                                    .build();
    }
}
