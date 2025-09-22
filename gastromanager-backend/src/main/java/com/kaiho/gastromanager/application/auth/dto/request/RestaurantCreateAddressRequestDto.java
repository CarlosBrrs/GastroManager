package com.kaiho.gastromanager.application.auth.dto.request;

public record RestaurantCreateAddressRequestDto(String street, String city, String postalCode, String country) {
}
