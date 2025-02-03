package com.kaiho.gastromanager.infrastructure.restaurant.input.rest;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.application.restaurant.handler.RestaurantHandler;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("${api.endpoint.base-url}/restaurants")
@AllArgsConstructor
//@RestaurantDomainRestController
public class RestaurantRestController {

    private final RestaurantHandler restaurantHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<List<RestaurantResponseDto>>> getAllRestaurants() {
        ApiGenericResponse<List<RestaurantResponseDto>> handlerResponse = restaurantHandler.getAllRestaurants();
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{restaurantUuid}")
    public ResponseEntity<ApiGenericResponse<RestaurantResponseDto>> getRestaurantByUuid(@PathVariable UUID restaurantUuid) {
        ApiGenericResponse<RestaurantResponseDto> handlerResponse = restaurantHandler.getRestaurantByUUID(restaurantUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto, @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(restaurantHandler.createRestaurant(restaurantRequestDto, user.getUuid()), HttpStatus.CREATED);
    }

    @PutMapping("/{restaurantUuid}")
    public ResponseEntity<ApiGenericResponse<RestaurantResponseDto>> updateRestaurant(
            @PathVariable UUID restaurantUuid, @RequestBody RestaurantRequestDto restaurantRequestDto, @AuthenticationPrincipal User user) {
        ApiGenericResponse<RestaurantResponseDto> handlerResponse = restaurantHandler.updateRestaurant(restaurantUuid, restaurantRequestDto, user.getUuid());
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

}
