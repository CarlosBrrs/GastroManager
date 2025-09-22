package com.kaiho.gastromanager.domain.menu.spi;

import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MenuPersistencePort {
    boolean menuExistsByName(String name, UUID uuid);

    UUID createMenu(Menu menu);

    Page<Menu> getAllMenus(MenuSearchCriteria criteria, Pageable pageable);

    Optional<Menu> getMenuByUuid(UUID uuid);

    Menu updateMenu(UUID uuid, Menu menu);

    boolean existsById(UUID menuUuid);
}
