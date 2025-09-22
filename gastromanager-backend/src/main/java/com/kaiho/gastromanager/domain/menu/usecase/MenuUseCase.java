package com.kaiho.gastromanager.domain.menu.usecase;

import com.kaiho.gastromanager.domain.menu.api.MenuServicePort;
import com.kaiho.gastromanager.domain.menu.exception.MenuAlreadyExistsException;
import com.kaiho.gastromanager.domain.menu.exception.MenuDoesNotExistException;
import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.domain.menu.spi.MenuPersistencePort;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Service
public class MenuUseCase implements MenuServicePort {

    private final MenuPersistencePort menuPersistencePort;

    @Override
    @Transactional
    public UUID createMenu(Menu menu) {
        if (menuPersistencePort.menuExistsByName(menu.getName(), menu.getRestaurant().getUuid())) {
            throw new MenuAlreadyExistsException(menu.getName());
        }
        return menuPersistencePort.createMenu(menu);
    }

    @Override
    public Page<Menu> getAllMenus(MenuSearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.sortDirection().equalsIgnoreCase("desc") ? DESC : ASC,
                criteria.sortBy()
        );
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
        return menuPersistencePort.getAllMenus(criteria, pageable);
    }

    @Override
    public Menu getMenuById(UUID uuid) {
        return menuPersistencePort.getMenuByUuid(uuid)
                                  .orElseThrow(() -> new MenuDoesNotExistException(uuid.toString()));
    }

    @Override
    public Menu updateMenu(UUID uuid, Menu menu) {
        Menu menuById = menuPersistencePort.getMenuByUuid(uuid)
                                           .orElseThrow(() -> new MenuDoesNotExistException(uuid.toString()));
        if (!menuById.getName().equals(menu.getName()) &&
                menuPersistencePort.menuExistsByName(menu.getName(), menu.getRestaurant().getUuid())) {
            throw new MenuAlreadyExistsException(menu.getName());
        }
        return menuPersistencePort.updateMenu(uuid, menu);
    }

    @Override
    public boolean existsById(UUID menuUuid) {
        return menuPersistencePort.existsById(menuUuid);
    }
}
