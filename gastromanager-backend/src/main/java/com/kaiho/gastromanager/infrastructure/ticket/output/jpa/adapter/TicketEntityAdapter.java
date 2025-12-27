package com.kaiho.gastromanager.infrastructure.ticket.output.jpa.adapter;

import com.kaiho.gastromanager.domain.order.exception.OrderDoesNotExistException;
import com.kaiho.gastromanager.domain.ticket.model.Ticket;
import com.kaiho.gastromanager.domain.ticket.model.TicketItem;
import com.kaiho.gastromanager.domain.ticket.spi.TicketPersistencePort;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.ticket.output.jpa.mapper.TicketEntityMapper;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Adaptador de infraestructura para tickets.
 * Implementa la obtención de datos desde la base de datos y la generación del PDF usando OpenPDF.
 * Diseñado para generar tickets compactos de uso interno para cocina.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class TicketEntityAdapter implements TicketPersistencePort {

    private final OrderEntityRepository orderEntityRepository;
    private final TicketEntityMapper ticketEntityMapper;

    @Override
    public Ticket getTicketData(UUID orderUuid, UUID restaurantUuid) {
        OrderEntity orderEntity = orderEntityRepository.findById(orderUuid, restaurantUuid)
                                                       .orElseThrow(() -> new OrderDoesNotExistException(orderUuid));

        // 2. Mapear OrderEntity a Ticket usando el mapper
        return ticketEntityMapper.toDomain(orderEntity);
    }

    @Override
    public byte[] generatePdf(Ticket ticket) {
        log.info("Generating PDF ticket for order: {}", ticket.getOrderCode());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // 1. Crear documento compacto con márgenes mínimos
            Document document = new Document(PageSize.A7, 10, 10, 10, 10); // Márgenes pequeños
            PdfWriter.getInstance(document, outputStream);

            // 2. Abrir documento
            document.open();

            // 3. Agregar contenido del ticket (versión compacta)
            addCompactHeader(document, ticket);
            addCompactItems(document, ticket);
            addCompactFooter(document, ticket);

            // 4. Cerrar documento
            document.close();

            log.info("PDF ticket generated successfully for order: {}", ticket.getOrderCode());

        } catch (DocumentException e) {
            log.error("Error generating PDF ticket for order: {}", ticket.getOrderCode(), e);
            throw new RuntimeException("Error generating PDF ticket", e);
        }

        return outputStream.toByteArray();
    }

    // ========== Métodos para ticket compacto de cocina ==========

    /**
     * Encabezado compacto: restaurante, orden, fecha/hora y notas generales.
     */
    private void addCompactHeader(Document document, Ticket ticket) throws DocumentException {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        Font noteFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);

        // Nombre del restaurante (centrado)
        Paragraph restaurantName = new Paragraph(ticket.getRestaurantName(), boldFont);
        restaurantName.setAlignment(Element.ALIGN_CENTER);
        document.add(restaurantName);

        // Línea separadora
        addThinSeparator(document);

        // Info de la orden en una sola línea
        // Convertir de UTC a zona horaria local del sistema
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")
                                                       .withZone(ZoneId.systemDefault()); // Usa la zona horaria del servidor
        String formattedDate = formatter.format(ticket.getOrderDate());

        String orderInfo = String.format("ORDEN: %s | %s", ticket.getOrderCode(), formattedDate);
        Paragraph orderLine = new Paragraph(orderInfo, normalFont);
        orderLine.setAlignment(Element.ALIGN_CENTER);
        document.add(orderLine);

        // Mesa y cliente en una línea (si existen)
        StringBuilder details = new StringBuilder();
        if (ticket.getTableNumber() != null && !ticket.getTableNumber().isEmpty()) {
            details.append("Mesa: ").append(ticket.getTableNumber());
        }
        if (ticket.getCustomerName() != null && !ticket.getCustomerName().isEmpty()) {
            if (details.length() > 0) details.append(" | ");
            details.append("Cliente: ").append(ticket.getCustomerName());
        }

        if (details.length() > 0) {
            Paragraph detailsLine = new Paragraph(details.toString(), normalFont);
            detailsLine.setAlignment(Element.ALIGN_CENTER);
            document.add(detailsLine);
        }

        addThinSeparator(document);

        // Notas generales de la orden (si existen)
        if (ticket.getCustomerNotes() != null && !ticket.getCustomerNotes().isEmpty()) {
            Paragraph notesLabel = new Paragraph("NOTA: " + ticket.getCustomerNotes(), noteFont);
            notesLabel.setAlignment(Element.ALIGN_CENTER);
            document.add(notesLabel);
            addThinSeparator(document);
        }
    }

    /**
     * Items con tabla real - cada item es una fila.
     * Bordes punteados para ahorrar tinta.
     * Columnas: Cant | Producto | P.Unit | Subtotal
     */
    private void addCompactItems(Document document, Ticket ticket) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6, Color.BLACK);
        Font itemFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Color.BLACK);
        Font quantityFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Color.BLACK); // 3px menos que itemFont (7-3=4)
        Font noteFont = FontFactory.getFont(FontFactory.HELVETICA, 5, Color.DARK_GRAY);

        // Crear tabla con 4 columnas: Cant | Producto | P.Unit | Subtotal
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(2);

        // Anchos ajustados: Cant=12%, Producto=44%, P.Unit=22%, Subtotal=22%
        table.setWidths(new float[]{12f, 44f, 22f, 22f});

        // ===== ENCABEZADOS DE LA TABLA =====
        addTableHeaderCell(table, "Cant", headerFont, Element.ALIGN_CENTER);
        addTableHeaderCell(table, "Producto", headerFont, Element.ALIGN_LEFT);
        addTableHeaderCell(table, "P.Unit", headerFont, Element.ALIGN_RIGHT);
        addTableHeaderCell(table, "Subtotal", headerFont, Element.ALIGN_RIGHT);

        // ===== FILAS DE ITEMS =====
        for (TicketItem item : ticket.getItems()) {
            // Columna 1: Cantidad (centrado) con fuente más pequeña
            addTableItemCell(table, item.getQuantity() + "x", quantityFont, Element.ALIGN_CENTER);

            // Columna 2: Producto + Notas (si hay) - SIN TRUNCAR, permitir wrap completo
            String productText = item.getProductName(); // Sin truncar

            // Agregar notas en la misma celda si existen
            if (item.getNotes() != null && !item.getNotes().isEmpty()) {
                productText += "\n* " + item.getNotes(); // Sin truncar las notas
            }
            addTableItemCell(table, productText, itemFont, Element.ALIGN_LEFT);

            // Columna 3: Precio unitario (derecha) - Formato más compacto
            addTableItemCell(table, formatCompactCurrency(item.getUnitPrice()), itemFont, Element.ALIGN_RIGHT);

            // Columna 4: Subtotal (derecha) - Formato más compacto
            addTableItemCell(table, formatCompactCurrency(item.getSubtotal()), itemFont, Element.ALIGN_RIGHT);
        }

        document.add(table);
        addThinSeparator(document);
    }

    /**
     * Crea una celda de encabezado con fondo blanco y bordes punteados.
     */
    private void addTableHeaderCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(3);
        cell.setBackgroundColor(Color.WHITE);

        // Bordes punteados para ahorrar tinta
        cell.setBorderWidth(0.5f);
        cell.setBorderColor(Color.GRAY);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderWidthBottom(1f); // Línea más gruesa abajo del header

        table.addCell(cell);
    }

    /**
     * Crea una celda de item con fondo blanco y bordes punteados.
     */
    private void addTableItemCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPadding(3);
        cell.setBackgroundColor(Color.WHITE);

        // Bordes punteados delgados
        cell.setBorderWidth(0.3f);
        cell.setBorderColor(Color.LIGHT_GRAY);
        cell.setBorder(Rectangle.BOX);

        // Permitir que el texto se ajuste en múltiples líneas si es necesario
        cell.setNoWrap(false);

        table.addCell(cell);
    }

    /**
     * Footer compacto: solo el total.
     */
    private void addCompactFooter(Document document, Ticket ticket) throws DocumentException {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

        // Total (destacado, alineado a la derecha)
        Paragraph total = new Paragraph("TOTAL: " + formatCurrency(ticket.getTotalAmount()), boldFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(2);
        document.add(total);
    }

    /**
     * Separador delgado para tickets compactos.
     */
    private void addThinSeparator(Document document) throws DocumentException {
        Font separatorFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);
        Paragraph separator = new Paragraph("- - - - - - - - - - - - - - - - - -", separatorFont);
        separator.setAlignment(Element.ALIGN_CENTER);
        separator.setSpacingBefore(1);
        separator.setSpacingAfter(1);
        document.add(separator);
    }


    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return String.format("$%,.2f", amount);
    }

    /**
     * Formatea la moneda de manera más compacta para tablas pequeñas.
     * Sin separador de miles para ahorrar espacio.
     */
    private String formatCompactCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0";
        }
        // Sin decimales si es un número entero
        if (amount.stripTrailingZeros().scale() <= 0) {
            return String.format("$%,.0f", amount);
        }
        return String.format("$%,.0f", amount);
    }
}

