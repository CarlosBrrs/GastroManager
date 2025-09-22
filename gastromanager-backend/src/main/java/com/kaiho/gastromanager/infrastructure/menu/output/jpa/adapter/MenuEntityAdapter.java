package com.kaiho.gastromanager.infrastructure.menu.output.jpa.adapter;

import com.kaiho.gastromanager.domain.menu.exception.MenuDoesNotExistException;
import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.domain.menu.spi.MenuPersistencePort;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.mapper.MenuEntityMapper;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.repository.MenuEntityRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static com.kaiho.gastromanager.infrastructure.menu.output.specification.MenuEntitySpecification.buildSpecification;

@RequiredArgsConstructor
@Repository
public class MenuEntityAdapter implements MenuPersistencePort {

    private final MenuEntityRepository menuEntityRepository;
    private final MenuEntityMapper menuEntityMapper;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Page<Menu> getAllMenus(MenuSearchCriteria criteria, Pageable pageable) {
        Specification<MenuEntity> spec = buildSpecification(criteria);
        Page<MenuEntity> entityList = menuEntityRepository.findAll(spec, pageable);
        return entityList.map(menuEntityMapper::toDomain);
    }

    @Override
    public boolean menuExistsByName(String name, UUID restaurantUuid) {
        return menuEntityRepository.existsByName(name, restaurantUuid);
    }

    @Override
    public UUID createMenu(Menu menu) {
        MenuEntity entity = menuEntityMapper.toEntity(menu);
        MenuEntity saved = menuEntityRepository.save(entity);
        return saved.getUuid();
    }

    @Override
    public Optional<Menu> getMenuByUuid(UUID uuid) {
        UUID currentRestaurant = getCurrentRestaurant();
        Optional<Menu> menu = menuEntityRepository.findById(uuid, currentRestaurant)
                                                  .map(menuEntityMapper::toDomain);
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(currentRestaurant)
                                                                      .orElseThrow(() -> new RestaurantDoesNotExistException(currentRestaurant.toString()));
        Restaurant restaurant = restaurantEntityMapper.toDomain(restaurantEntity);
        menu.ifPresent(m -> m.setRestaurant(restaurant));
        return menu;
    }

    @Override
    public Menu updateMenu(UUID uuid, Menu menu) {
        MenuEntity existingEntity = menuEntityRepository.findById(uuid, menu.getRestaurant().getUuid()).orElseThrow(() -> new MenuDoesNotExistException(uuid.toString()));

        existingEntity.setName(menu.getName());
        existingEntity.setDescription(menu.getDescription());

        MenuEntity saved = menuEntityRepository.save(existingEntity);
        return menuEntityMapper.toDomain(saved);
    }

    @Override
    public boolean existsById(UUID menuUuid) {
        return menuEntityRepository.existsByUuidAndRestaurantUuid(menuUuid, getCurrentRestaurant());
    }
}
