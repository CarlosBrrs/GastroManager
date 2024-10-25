package com.kaiho.gastromanager.infrastructure.order.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, UUID> {
}
