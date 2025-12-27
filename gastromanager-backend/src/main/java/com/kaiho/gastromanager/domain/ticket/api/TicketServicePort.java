package com.kaiho.gastromanager.domain.ticket.api;

import java.util.UUID;

public interface TicketServicePort {

    byte[] generateTicketPdf(UUID orderUuid);
}

