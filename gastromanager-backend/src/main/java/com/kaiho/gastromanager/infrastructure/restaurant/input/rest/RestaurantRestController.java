package com.kaiho.gastromanager.infrastructure.restaurant.input.rest;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantCreateRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantDetailResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.UserRestaurantAccessResponseDto;
import com.kaiho.gastromanager.application.restaurant.handler.RestaurantHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantRestController {

    private final RestaurantHandler restaurantHandler;

    @GetMapping("/my-access")
    public ResponseEntity<ApiGenericResponse<List<UserRestaurantAccessResponseDto>>> getUserRestaurants() {
        ApiGenericResponse<List<UserRestaurantAccessResponseDto>> response = restaurantHandler.getUserRestaurants();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{restaurantUuid}")
    public ResponseEntity<ApiGenericResponse<RestaurantDetailResponseDto>> getRestaurantDetails(
            @PathVariable UUID restaurantUuid) {
        ApiGenericResponse<RestaurantDetailResponseDto> response = restaurantHandler.getRestaurantDetails(restaurantUuid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createRestaurant(@RequestBody RestaurantCreateRequestDto restaurantCreateRequestDto) {
        ApiGenericResponse<UUID> response = restaurantHandler.createRestaurant(restaurantCreateRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

   /* private final MenuHandler menuHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<Page<MenuSummaryResponseDto>>> getAllMenus(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        MenuSearchCriteria criteria = MenuSearchCriteria.builder()
                                                        .search(search)
                                                        .sortBy(sort.split(",")[0])
                                                        .sortDirection(sort.split(",")[1])
                                                        .page(page)
                                                        .size(size)
                                                        .build();
        ApiGenericResponse<Page<MenuSummaryResponseDto>> handlerResponse = menuHandler.getAllMenus(criteria);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{menuUuid}")
    public ResponseEntity<ApiGenericResponse<MenuDetailResponseDto>> getMenuById(@PathVariable UUID menuUuid) {
        ApiGenericResponse<MenuDetailResponseDto> handlerResponse = menuHandler.getMenuById(menuUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createMenu(@RequestBody @Valid MenuRequestDto menuRequestDto) {
        return new ResponseEntity<>(menuHandler.createMenu(menuRequestDto), CREATED);
    }

    @PutMapping("/{menuUuid}")
    public ResponseEntity<ApiGenericResponse<MenuDetailResponseDto>> updateMenu(
            @PathVariable UUID menuUuid, @RequestBody MenuUpdateRequestDto menuRequestDto) {
        ApiGenericResponse<MenuDetailResponseDto> handlerResponse = menuHandler.updateMenu(menuUuid, menuRequestDto);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }*/
}
