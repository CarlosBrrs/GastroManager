package com.kaiho.gastromanager.application.auth.dto.request;

public record SignupRequestDto(String name, String lastname, String username, String password,
                               RestaurantCreateContactRequestDto contact,
                               SubscriptionRequestDto subscription) {
}
