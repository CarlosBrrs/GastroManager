package com.kaiho.gastromanager.domain.invoice.usecase;

import com.kaiho.gastromanager.domain.invoice.api.InvoiceServicePort;
import com.kaiho.gastromanager.domain.invoice.model.Invoice;
import com.kaiho.gastromanager.domain.invoice.spi.InvoicePersistencePort;
import com.kaiho.gastromanager.domain.invoiceitem.model.InvoiceItem;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.model.InvoicingStatus;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.orderitem.api.OrderItemServicePort;
import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kaiho.gastromanager.domain.order.model.InvoicingStatus.FULLY_INVOICED;
import static com.kaiho.gastromanager.domain.order.model.InvoicingStatus.PARTIALLY_INVOICED;
import static com.kaiho.gastromanager.domain.order.model.OperationalStatus.CANCELLED;
import static com.kaiho.gastromanager.domain.order.model.OperationalStatus.COMPLETED;
import static com.kaiho.gastromanager.domain.taxconfig.model.TaxType.IMPO_CONSUMO;
import static com.kaiho.gastromanager.domain.taxconfig.model.TaxType.IVA;

@Service
@RequiredArgsConstructor
public class InvoiceServiceUseCase implements InvoiceServicePort {

    private final InvoicePersistencePort invoicePersistencePort;
    private final OrderItemServicePort orderItemServicePort;
    private final OrderServicePort orderServicePort;

    @Override
    public List<Invoice> getAllInvoices() {
        return null;
    }

    @Override
    @Transactional
    public UUID generateInvoice(Invoice invoice) {
       /* // todo la orden no tiene el restaurant
        Order referenceOrder = invoice.getOrder();

        if (referenceOrder.getOperationalStatus() == CANCELLED || referenceOrder.getOperationalStatus() == COMPLETED) {
            throw new IllegalStateException("No se pueden generar facturas para ordenes completadas o canceladas");
        }

        List<Invoice> existingInvoices = invoicePersistencePort.getInvoicesByOrderUUID(referenceOrder.getUuid());

        Map<UUID, Integer> invoicedQuantities = calculateInvoicedQuantities(existingInvoices);

        // Validar cantidades del nuevo invoice
        validateQuantities(invoice, invoicedQuantities);

        calculateAmountsForInvoice(invoice);

        // todo duplicated code in invoiceitemusecase
        Restaurant restaurant = invoice.getRestaurant();
        TaxConfig taxConfig = restaurant.getConfig().isFranchise() ?
                restaurant.getTaxes().stream()
                          .filter(tc -> tc.getTaxType().equals(IVA))
                          .findFirst()
                          .orElseThrow(() -> new IllegalArgumentException("IVA TAX NOT FOUND")) :
                restaurant.getTaxes().stream()
                          .filter(tc -> tc.getTaxType().equals(IMPO_CONSUMO))
                          .findFirst()
                          .orElseThrow(() -> new IllegalArgumentException("IMPOCONSUMO TAX NOT FOUND"));
        // todo duplicated code in invoiceitemusecase

        invoice.setTaxRate(taxConfig.getTaxRate());

        UUID invoiceUuid = invoicePersistencePort.generateInvoice(invoice);
        updateOrderStatus(referenceOrder);

        return invoiceUuid;*/
        return null;
    }

    private void updateOrderStatus(Order referenceOrder) {
        InvoicingStatus newStatus;
        if (orderItemServicePort.getUninvoicedItemsByOrderUuid(referenceOrder.getUuid()).size() == 0) {
            newStatus = FULLY_INVOICED;
        } else {
            newStatus = PARTIALLY_INVOICED;
        }
//        orderServicePort.changeInvoicingStatus(referenceOrder, newStatus);
    }

    private void calculateAmountsForInvoice(Invoice invoice) {

/*        BigDecimal discount = invoice.getInvoiceItems().stream()
                                     .map(InvoiceItem::getDiscount)
                                     .reduce(BigDecimal.ZERO, BigDecimal::add)
                                     .setScale(2, RoundingMode.HALF_UP); // Redondeo final del descuento
        // Calcular el total directamente desde los OrderItems
        BigDecimal total = invoice.getInvoiceItems().stream()
                                  .map(item -> BigDecimal.valueOf(item.getOrderItem().getSellingPrice())
                                                         .multiply(BigDecimal.valueOf(item.getQuantity()))
                                                         .setScale(2, RoundingMode.HALF_UP))
                                  .reduce(BigDecimal.ZERO, BigDecimal::add).subtract(discount).add(invoice.getTipAmount());

        // Calcular subtotal, total_tax y total de la factura
        BigDecimal subtotal = invoice.getInvoiceItems().stream()
                                     .map(item -> item.getBasePrice()
                                                      .multiply(BigDecimal.valueOf(item.getQuantity()))
                                                      .setScale(2, RoundingMode.HALF_UP)) // Redondeo después de multiplicar
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTax = invoice.getInvoiceItems().stream()
                                     .map(item -> item.getTaxAmount()
                                                      .multiply(BigDecimal.valueOf(item.getQuantity()))
                                                      .setScale(2, RoundingMode.HALF_UP)) // Redondeo después de multiplicar
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Asignar valores a la factura
        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(totalTax);
        invoice.setAmount(total);
        invoice.setDiscount(discount);*/
    }

    private void validateQuantities(Invoice invoice, Map<UUID, Integer> invoicedQuantities) {
        invoice.getInvoiceItems().forEach(newInvoiceItem -> {
            OrderItem orderItem = newInvoiceItem.getOrderItem();
            int orderedQuantity = orderItem.getQuantity();
            int alreadyInvoicedQuantity = invoicedQuantities.getOrDefault(orderItem.getUuid(), 0);
            int newQuantity = newInvoiceItem.getQuantity();

/*            if (alreadyInvoicedQuantity + newQuantity > orderedQuantity) {
                throw new IllegalStateException("El producto " + orderItem.getProductItem().getName() + " excede la cantidad solicitada en la orden. Cantidad solicitada: " + orderedQuantity + ", ya facturada: " + alreadyInvoicedQuantity + ", intentando facturar: " + newQuantity);
            }*/
        });
    }

    private Map<UUID, Integer> calculateInvoicedQuantities(List<Invoice> existingInvoices) {
        return existingInvoices.stream()
                               .flatMap(existingInvoice -> existingInvoice.getInvoiceItems().stream())
                               .collect(Collectors.groupingBy(invoiceItem ->
                                               invoiceItem.getOrderItem().getUuid(),
                                       Collectors.summingInt(InvoiceItem::getQuantity))
                               );
    }

    @Override
    public List<Invoice> getInvoicesByOrderUuid(UUID orderUuid) {
        return invoicePersistencePort.getInvoicesByOrderUUID(orderUuid);
    }
}