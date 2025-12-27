package com.kaiho.gastromanager.domain.invoiceitem.usecase;

import com.kaiho.gastromanager.application.invoice.dto.request.InvoiceItemRequestDto;
import com.kaiho.gastromanager.domain.invoiceitem.api.InvoiceItemServicePort;
import com.kaiho.gastromanager.domain.invoiceitem.model.InvoiceItem;
import com.kaiho.gastromanager.domain.orderitem.api.OrderItemServicePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InvoiceItemUseCase implements InvoiceItemServicePort {
    private final OrderItemServicePort orderItemServicePort;
    private final RestaurantServicePort restaurantServicePort;

    @Override
    public List<InvoiceItem> getInvoiceItemsFromOrderItems(List<OrderItem> orderItems) {
        return null;
    }

    @Override
    public List<InvoiceItem> getInvoiceItemsFromOrderItemUuids(List<InvoiceItemRequestDto> invoiceItemRequestDtoList) {
        return null;
/*
        List<UUID> orderItemUuids = invoiceItemRequestDtoList.stream()
                                                             .map(InvoiceItemRequestDto::orderItemUuid)
                                                             .toList();
        List<OrderItem> orderItems = orderItemServicePort.getAllOrderItems(orderItemUuids);

        Restaurant restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());
        TaxConfig taxConfig = restaurant.getConfig().isFranchise() ?
                restaurant.getTaxes().stream()
                          .filter(tc -> tc.getTaxType().equals(IVA))
                          .findFirst()
                          .orElseThrow(() -> new IllegalArgumentException("IVA TAX NOT FOUND")) :
                restaurant.getTaxes().stream()
                          .filter(tc -> tc.getTaxType().equals(IMPO_CONSUMO))
                          .findFirst()
                          .orElseThrow(() -> new IllegalArgumentException("IMPOCONSUMO TAX NOT FOUND"));

        return invoiceItemRequestDtoList.stream()
                                        .map(orderItemRequestDto -> {
                                            OrderItem orderItem = orderItems.stream()
                                                                            .filter(item -> item.getUuid().equals(orderItemRequestDto.orderItemUuid()))
                                                                            .findFirst()
                                                                            .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + orderItemRequestDto.orderItemUuid()));


                                            BigDecimal sellingPrice = BigDecimal.valueOf(orderItem.getSellingPrice());
                                            BigDecimal taxRate = BigDecimal.valueOf(taxConfig.getTaxRate());
                                            BigDecimal basePrice = sellingPrice.divide(BigDecimal.ONE.add(taxRate), 2, RoundingMode.CEILING);
                                            BigDecimal taxAmount = basePrice.multiply((taxRate)).setScale(2, RoundingMode.CEILING);

                                            return InvoiceItem.builder()
                                                              .orderItem(orderItem)
                                                              .quantity(orderItemRequestDto.quantity())
                                                              .taxRate(taxConfig.getTaxRate())
                                                              .taxAmount(taxAmount)
                                                              .discount(calculateDiscount(orderItem))
                                                              .basePrice(basePrice)
                                                              .build();
                                        })
                                        .toList();
*/

    }

    private BigDecimal calculateDiscount(OrderItem orderItem) {
        return BigDecimal.ZERO;

    }
}
