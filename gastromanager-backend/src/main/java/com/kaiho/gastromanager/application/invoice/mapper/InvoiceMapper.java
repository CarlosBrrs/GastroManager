package com.kaiho.gastromanager.application.invoice.mapper;

import com.kaiho.gastromanager.application.invoice.dto.request.InvoiceRequestDto;
import com.kaiho.gastromanager.application.invoice.dto.response.InvoiceResponseDto;
import com.kaiho.gastromanager.domain.invoice.model.Invoice;
import com.kaiho.gastromanager.domain.invoiceitem.api.InvoiceItemServicePort;
import com.kaiho.gastromanager.domain.invoiceitem.model.InvoiceItem;
import com.kaiho.gastromanager.domain.orderitem.api.OrderItemServicePort;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.kaiho.gastromanager.domain.invoice.model.PaymentStatus.PENDING;
import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {

    private final OrderItemServicePort orderItemServicePort;
    private final RestaurantServicePort restaurantServicePort;
    private final InvoiceItemServicePort invoiceItemServicePort;

    public InvoiceResponseDto toResponse(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        return InvoiceResponseDto.builder()
                                 .uuid(invoice.getUuid())
                                 .status(invoice.getStatus())
                                 .amount(invoice.getAmount().doubleValue())
                                 .createdBy(invoice.getCreatedBy())
                                 .createdDate(invoice.getCreatedDate())
                                 .build();
    }

    public Invoice toDomain(InvoiceRequestDto invoiceRequestDto) {
        if (invoiceRequestDto == null) {
            return null;
        }
        Restaurant restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());

        List<InvoiceItem> invoiceItems = invoiceItemServicePort.getInvoiceItemsFromOrderItemUuids(invoiceRequestDto.invoiceItems());

        return Invoice.builder()
                      .restaurant(restaurant)
                      .customerName(invoiceRequestDto.customerName())
                      .customerEmail(invoiceRequestDto.customerEmail())
                      .customerPhone(invoiceRequestDto.customerPhone())
                      .tipAmount(BigDecimal.valueOf(invoiceRequestDto.tipAmount()))
                      .status(PENDING)
                      .invoiceItems(invoiceItems)
                      .build();
    }
}
