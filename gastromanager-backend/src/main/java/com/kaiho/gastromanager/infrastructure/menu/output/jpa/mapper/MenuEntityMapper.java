package com.kaiho.gastromanager.infrastructure.menu.output.jpa.mapper;

import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuEntityMapper {

    private final RestaurantEntityRepository restaurantEntityRepository;

    public Menu toDomain(MenuEntity menuEntity) {
        if (menuEntity == null) {
            return null;
        }
        return Menu.builder()
                   .uuid(menuEntity.getUuid())
                   .name(menuEntity.getName())
                   .description(menuEntity.getDescription())
                   .isEnabled(menuEntity.isEnabled())
                   .createdBy(menuEntity.getCreatedBy())
                   .createdDate(menuEntity.getCreatedDate())
                   .updatedBy(menuEntity.getUpdatedBy())
                   .updatedDate(menuEntity.getUpdatedDate())
                   .build();
    }

    public MenuEntity toEntity(Menu menu) {
        if (menu == null) {
            return null;
        }
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(menu.getRestaurant().getUuid())
                                                                      .orElseThrow(() -> new RestaurantDoesNotExistException(menu.getRestaurant().getUuid().toString()));
        return MenuEntity.builder()
                         .uuid(menu.getUuid())
                         .name(menu.getName())
                         .restaurant(restaurantEntity)
                         .isEnabled(menu.isEnabled())
                         .description(menu.getDescription())
                         .createdBy(menu.getCreatedBy())
                         .createdDate(menu.getCreatedDate())
                         .updatedBy(menu.getUpdatedBy())
                         .updatedDate(menu.getUpdatedDate())
                         .build();
    }
}
