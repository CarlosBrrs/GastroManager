package com.kaiho.gastromanager.application.menu.mapper;

import com.kaiho.gastromanager.application.menu.dto.request.MenuRequestDto;
import com.kaiho.gastromanager.application.menu.dto.request.MenuUpdateRequestDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuDetailResponseDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuSummaryResponseDto;
import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class MenuMapper {

    public Menu toDomain(MenuRequestDto dto) {
        return Menu.builder()
                   .name(dto.name())
                   .description(dto.description())
                   .isEnabled(true)
                   .restaurant(Restaurant.builder().uuid(getCurrentRestaurant()).build())
                   .build();
    }

    public MenuDetailResponseDto toResponseDetail(Menu menu) {
        return MenuDetailResponseDto.builder()
                                    .uuid(menu.getUuid())
                                    .name(menu.getName())
                                    .description(menu.getDescription())
                                    .isEnabled(menu.isEnabled())
                                    .createdBy(menu.getCreatedBy())
                                    .createdDate(menu.getCreatedDate())
                                    .updatedBy(menu.getUpdatedBy())
                                    .updatedDate(menu.getUpdatedDate())
                                    .build();
    }

    public MenuSummaryResponseDto toResponseSummary(Menu menu) {
        if (menu == null) {
            return null;
        }
        return MenuSummaryResponseDto.builder()
                                     .uuid(menu.getUuid())
                                     .name(menu.getName())
                                     .description(menu.getDescription())
                                     .createdBy(menu.getCreatedBy())
                                     .createdDate(menu.getCreatedDate())
                                     .updatedBy(menu.getUpdatedBy())
                                     .updatedDate(menu.getUpdatedDate())
                                     .build();
    }

    public Menu toDomain(MenuUpdateRequestDto menuRequestDto) {
        if (menuRequestDto == null) {
            return null;
        }
        return Menu.builder()
                   .name(menuRequestDto.name())
                   .description(menuRequestDto.description())
                   .restaurant(Restaurant.builder().uuid(getCurrentRestaurant()).build())
                   .build();
    }
}

