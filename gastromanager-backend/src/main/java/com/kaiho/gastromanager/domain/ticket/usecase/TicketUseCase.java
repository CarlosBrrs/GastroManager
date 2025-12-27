package com.kaiho.gastromanager.domain.ticket.usecase;

import com.kaiho.gastromanager.domain.ticket.api.TicketServicePort;
import com.kaiho.gastromanager.domain.ticket.model.Ticket;
import com.kaiho.gastromanager.domain.ticket.spi.TicketPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Service
@RequiredArgsConstructor
public class TicketUseCase implements TicketServicePort {

    private final TicketPersistencePort ticketPersistencePort;

    @Override
    public byte[] generateTicketPdf(UUID orderUuid) {
        Ticket ticketData = ticketPersistencePort.getTicketData(orderUuid, getCurrentRestaurant());

        // 2. TODO: Aquí se podrían agregar validaciones de negocio adicionales
        // - Validar que la orden esté en un estado válido para imprimir
        // - Aplicar reglas de negocio específicas del restaurante
        // - Registrar en auditoría que se imprimió el ticket
        // - etc.

        // 3. Generar el PDF
        return ticketPersistencePort.generatePdf(ticketData);
    }
}

