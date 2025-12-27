package com.kaiho.gastromanager.application.ticket.handler;

import com.kaiho.gastromanager.domain.ticket.api.TicketServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketHandlerImpl implements TicketHandler {

    private final TicketServicePort ticketServicePort;

    /**
     * Genera un ticket PDF para una orden.
     * <p>
     * Flujo:
     * 1. Obtiene el restaurante actual del contexto
     * 2. Llama al service port (capa de dominio) para generar el PDF
     * 3. Mapea el resultado (byte[]) al DTO de respuesta
     * 4. Retorna la respuesta genérica de la API
     */
    @Override
    public byte[] generateTicketPdf(UUID orderUuid) {

        // 2. Llamar al service port para generar el PDF (lógica de negocio)
        return ticketServicePort.generateTicketPdf(orderUuid);

        // 4. Retornar respuesta exitosa
//        return buildSuccessResponse("Ticket generated successfully", pdfBytes);
    }
}
