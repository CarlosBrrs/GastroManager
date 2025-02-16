package com.kaiho.gastromanager.infrastructure.subscriptionplan.input.rest;

import com.kaiho.gastromanager.application.subscriptionplan.dto.response.SubscriptionPlanResponseDto;
import com.kaiho.gastromanager.application.subscriptionplan.handler.SubscriptionPlanHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscription-plans")
@AllArgsConstructor
public class SubscriptionPlanRestController {

    private final SubscriptionPlanHandler subscriptionPlanHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<List<SubscriptionPlanResponseDto>>> getAllSubscriptionPlans() {
        ApiGenericResponse<List<SubscriptionPlanResponseDto>> handlerResponse = subscriptionPlanHandler.getAllSubscriptionPlans();
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{subscriptionPlan}")
    public ResponseEntity<ApiGenericResponse<SubscriptionPlanResponseDto>> getSubscriptionPlanByUUID(@PathVariable UUID subscriptionPlan) {
        ApiGenericResponse<SubscriptionPlanResponseDto> handlerResponse = subscriptionPlanHandler.getSubscriptionPlanByUUID(subscriptionPlan);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }


}
