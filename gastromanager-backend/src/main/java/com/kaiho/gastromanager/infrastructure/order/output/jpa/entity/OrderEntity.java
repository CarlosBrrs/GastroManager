package com.kaiho.gastromanager.infrastructure.order.output.jpa.entity;

import com.kaiho.gastromanager.domain.order.model.OrderStatus;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderEntity extends Auditable implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private UserEntity user;

    private double totalPrice;
    private String customerNotes;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItemEntity orderItemEntity) {
        orderItems.add(orderItemEntity);
        orderItemEntity.setOrder(this);
    }

}
