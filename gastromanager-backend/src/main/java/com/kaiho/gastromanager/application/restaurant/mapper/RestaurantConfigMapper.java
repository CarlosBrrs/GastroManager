package com.kaiho.gastromanager.application.restaurant.mapper;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantConfigCreateRequestDto;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import org.springframework.stereotype.Component;

@Component
public class RestaurantConfigMapper {
    public RestaurantConfig toDomain(RestaurantConfigCreateRequestDto configs) {
        if (configs == null) {
            return null;
        }
        return RestaurantConfig.builder().isFranchise(configs.isFranchise())
                               .payBeforeOrder(configs.payBeforeOrder())
                               .build();
    }
}
