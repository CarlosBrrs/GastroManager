package com.kaiho.gastromanager.domain.ticket.spi;

import com.kaiho.gastromanager.domain.ticket.model.Ticket;

import java.util.UUID;

public interface TicketPersistencePort {

    Ticket getTicketData(UUID orderUuid, UUID restaurantUuid);

    byte[] generatePdf(Ticket ticket);
}

