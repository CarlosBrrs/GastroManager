package com.kaiho.gastromanager.domain.pricing.usecase;

import com.kaiho.gastromanager.domain.orderitem.model.OrderItem;
import com.kaiho.gastromanager.domain.pricing.api.PricingServicePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PricingServiceUseCase implements PricingServicePort {
    @Override
    public double calculateOrderTotal(List<OrderItem> orderItems, Map<UUID, Double> productItemPriceMap) {
        double totalAmount = 0.0;

        //Here would go any logic to apply general discounts, or be more detailed about the order rubrics
        for (OrderItem orderItem : orderItems) {
            Double unitPrice = productItemPriceMap.get(orderItem.getProductItem().getUuid());
            if (unitPrice == null) {
                throw new IllegalArgumentException("El producto con UUID " + orderItem.getProductItem().getUuid() + " no se encuentra disponible.");
            }
            totalAmount += unitPrice * orderItem.getQuantity();

        }
        return totalAmount;
    }
}
