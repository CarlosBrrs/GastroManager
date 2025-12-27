package com.kaiho.gastromanager.domain.report.usecase.sales;

import com.kaiho.gastromanager.domain.report.datacontext.sales.ProductSalesDataContext;
import com.kaiho.gastromanager.domain.report.model.sales.product.ProductSale;
import com.kaiho.gastromanager.domain.report.usecase.MetricCalculator;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductSalesMetricCalculator implements MetricCalculator<ProductSalesDataContext, List<ProductSale>> {

    @Override
    public List<ProductSale> calculate(ProductSalesDataContext dataContext) {
        List<OrderItemEntity> orderItems = dataContext.orderItems();

        if (orderItems.isEmpty()) {
            return List.of();
        }

        Map<String, ProductMetrics> productMetricsMap = groupSalesByProduct(orderItems);
        BigDecimal totalSalesAllProducts = calculateTotalSales(productMetricsMap);
        return buildProductSalesList(productMetricsMap, totalSalesAllProducts);
    }

    private Map<String, ProductMetrics> groupSalesByProduct(List<OrderItemEntity> orderItems) {
        Map<String, ProductMetrics> productMetricsMap = new HashMap<>();

        for (OrderItemEntity item : orderItems) {
            String productUuid = item.getProduct().getUuid().toString();
            String productName = item.getProduct().getName();
            BigDecimal itemSales = item.getSubtotal();
            int quantity = item.getQuantity();

            productMetricsMap.computeIfAbsent(productUuid, k -> new ProductMetrics(productUuid, productName));
            ProductMetrics metrics = productMetricsMap.get(productUuid);
            metrics.addSale(quantity, itemSales);
        }

        return productMetricsMap;
    }

    private BigDecimal calculateTotalSales(Map<String, ProductMetrics> productMetricsMap) {
        return productMetricsMap.values().stream()
                                .map(ProductMetrics::getTotalSales)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<ProductSale> buildProductSalesList(
            Map<String, ProductMetrics> productMetricsMap,
            BigDecimal totalSalesAllProducts) {

        List<ProductSale> productSales = productMetricsMap.values().stream()
                                                          .map(metrics -> buildProductSale(metrics, totalSalesAllProducts))
                                                          .sorted(Comparator.comparing(ProductSale::totalSales).reversed())
                                                          .toList();

        // Ajustar porcentajes para que sumen exactamente 100%
        return adjustPercentagesToSum100(productSales);
    }

    private List<ProductSale> adjustPercentagesToSum100(List<ProductSale> productSales) {
        if (productSales.isEmpty()) {
            return productSales;
        }

        // Calcular la suma actual de porcentajes
        double currentSum = productSales.stream()
                                        .mapToDouble(ProductSale::salesPercentage)
                                        .sum();

        // Si ya suma 100%, no hacer nada
        if (Math.abs(currentSum - 100.0) < 0.01) {
            return productSales;
        }

        // Calcular la diferencia y ajustar el último elemento (el de menor venta)
        double difference = roundDouble(100.0 - currentSum);

        // Crear una nueva lista con el último elemento ajustado
        ArrayList<ProductSale> adjustedList = new ArrayList<>(productSales);
        int lastIndex = adjustedList.size() - 1;
        ProductSale lastProduct = adjustedList.get(lastIndex);

        double adjustedPercentage = roundDouble(lastProduct.salesPercentage() + difference);

        ProductSale adjustedLastProduct = ProductSale.builder()
                                                     .productUuid(lastProduct.productUuid())
                                                     .productName(lastProduct.productName())
                                                     .unitsSold(lastProduct.unitsSold())
                                                     .totalSales(lastProduct.totalSales())
                                                     .salesPercentage(adjustedPercentage)
                                                     .build();

        adjustedList.set(lastIndex, adjustedLastProduct);
        return adjustedList;
    }

    private ProductSale buildProductSale(ProductMetrics metrics, BigDecimal totalSalesAllProducts) {
        Double salesPercentage = calculateSalesPercentage(metrics.getTotalSales(), totalSalesAllProducts);

        return ProductSale.builder()
                          .productUuid(metrics.getProductUuid())
                          .productName(metrics.getProductName())
                          .unitsSold(metrics.getUnitsSold())
                          .totalSales(metrics.getTotalSales())
                          .salesPercentage(salesPercentage)
                          .build();
    }

    private Double calculateSalesPercentage(BigDecimal productSales, BigDecimal totalSales) {
        if (totalSales.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return productSales.divide(totalSales, 4, RoundingMode.HALF_UP)
                           .multiply(BigDecimal.valueOf(100))
                           .setScale(2, RoundingMode.HALF_UP)
                           .doubleValue();
    }

    private double roundDouble(double value) {
        return BigDecimal.valueOf(value)
                         .setScale(2, RoundingMode.HALF_UP)
                         .doubleValue();
    }

    @Override
    public String getMetricName() {
        return "productSales";
    }
}

