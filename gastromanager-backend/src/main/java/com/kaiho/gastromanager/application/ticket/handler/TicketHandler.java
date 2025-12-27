package com.kaiho.gastromanager.application.ticket.handler;

import java.util.UUID;

public interface TicketHandler {
    byte[] generateTicketPdf(UUID orderUuid);
}
