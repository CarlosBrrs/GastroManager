package com.kaiho.gastromanager.application.restaurant.dto.request;

import com.kaiho.gastromanager.application.auth.dto.request.AddressRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.ContactRequestDto;
import lombok.Builder;

@Builder
public record RestaurantRequestDto(String name, AddressRequestDto address, String description,
                                   ContactRequestDto contact) {
}
