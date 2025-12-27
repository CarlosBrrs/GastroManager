package com.kaiho.gastromanager.domain.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Ticket {

    private UUID orderUuid;
    private String orderCode;
    private Instant orderDate;

    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhone;

    private String customerName;
    private String tableNumber;
    private String customerNotes;

    private List<TicketItem> items;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal tip;
    private BigDecimal totalAmount;
    private BigDecimal totalPaid;
    private BigDecimal remainingToPay;

    private String paymentStatus;
    private String operationalStatus;
}

