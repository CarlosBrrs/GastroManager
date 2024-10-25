package com.kaiho.gastromanager.domain.orderitem.usecase;

import com.kaiho.gastromanager.domain.orderitem.api.OrderItemServicePort;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemUseCase implements OrderItemServicePort {

    private final ProductItemServicePort productItemServicePort;
}
