package com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity;

import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderItemEntity extends Auditable implements Serializable {

    @ManyToOne
    @JoinColumn(name = "order_uuid", nullable = false)
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "product_item_uuid", nullable = false)
    private ProductItemEntity productItem;

    private double unitPrice;
    private int quantity;

}
