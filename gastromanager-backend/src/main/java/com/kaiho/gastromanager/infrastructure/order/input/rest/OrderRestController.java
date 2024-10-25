package com.kaiho.gastromanager.infrastructure.order.input.rest;

import com.kaiho.gastromanager.application.order.dto.request.OrderRequestDto;
import com.kaiho.gastromanager.application.order.dto.response.OrderResponseDto;
import com.kaiho.gastromanager.application.order.handler.OrderHandler;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.endpoint.base-url}/orders")
@AllArgsConstructor
public class OrderRestController {

    private final OrderHandler orderHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<List<OrderResponseDto>>> getAllOrders() {
        ApiGenericResponse<List<OrderResponseDto>> handlerResponse = orderHandler.getAllOrders();
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{orderUuid}")
    public ResponseEntity<ApiGenericResponse<OrderResponseDto>> getOrderByUUID(@PathVariable UUID orderUuid) {
        ApiGenericResponse<OrderResponseDto> handlerResponse = orderHandler.getOrderByUUID(orderUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createOrder(@RequestBody OrderRequestDto orderRequestDto, @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(orderHandler.createOrder(orderRequestDto, user.uuid()), HttpStatus.CREATED);
    }

    @PutMapping("/{orderUuid}")
    public ResponseEntity<ApiGenericResponse<OrderResponseDto>> updateOrder(
            @PathVariable UUID orderUuid, @RequestBody OrderRequestDto orderRequestDto) {
        ApiGenericResponse<OrderResponseDto> handlerResponse = orderHandler.updateOrder(orderUuid, orderRequestDto);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }
}
