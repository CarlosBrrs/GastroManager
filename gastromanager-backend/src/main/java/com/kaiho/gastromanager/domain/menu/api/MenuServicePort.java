package com.kaiho.gastromanager.domain.menu.api;

import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MenuServicePort {
    UUID createMenu(Menu menu);

    Page<Menu> getAllMenus(MenuSearchCriteria criteria);

    Menu getMenuById(UUID menuUuid);

    Menu updateMenu(UUID menuUuid, Menu menu);

    boolean existsById(UUID menuUuid);
}
