package com.kaiho.gastromanager.infrastructure.submenu.output.jpa.mapper;

import com.kaiho.gastromanager.domain.menu.exception.MenuDoesNotExistException;
import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.repository.MenuEntityRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity.SubmenuEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class SubmenuEntityMapper {

    private final RestaurantEntityRepository restaurantEntityRepository;
    private final MenuEntityRepository menuEntityRepository;

    public Submenu toDomain(SubmenuEntity submenuEntity) {
        if (submenuEntity == null) {
            return null;
        }
        return Submenu.builder()
                      .uuid(submenuEntity.getUuid())
                      .name(submenuEntity.getName())
                      .isEnabled(submenuEntity.isEnabled())
                      .description(submenuEntity.getDescription())
                      .menu(Menu.builder()
                                .uuid(submenuEntity.getMenu().getUuid())
                                .name(submenuEntity.getMenu().getName())
                                .build())
                      .createdDate(submenuEntity.getCreatedDate())
                      .createdBy(submenuEntity.getCreatedBy())
                      .updatedDate(submenuEntity.getUpdatedDate())
                      .updatedBy(submenuEntity.getUpdatedBy())
                      .build();
    }

    public SubmenuEntity toEntity(Submenu submenu) {
        if (submenu == null) {
            return null;
        }
        UUID menuUuid = submenu.getMenu().getUuid();
        MenuEntity menuEntity = menuEntityRepository.findById(menuUuid, getCurrentRestaurant())
                                                    .orElseThrow(() -> new MenuDoesNotExistException(menuUuid.toString()));

        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(getCurrentRestaurant())
                                                                      .orElseThrow(() -> new IllegalArgumentException("Restaurant does not exist"));
        SubmenuEntity submenuEntity = SubmenuEntity.builder()
                                                   .uuid(submenu.getUuid())
                                                   .name(submenu.getName())
                                                   .isEnabled(submenu.isEnabled())
                                                   .description(submenu.getDescription())
                                                   .build();
        menuEntity.addSubmenu(submenuEntity);
        restaurantEntity.addSubmenu(submenuEntity);
        return submenuEntity;
    }
}
