package com.kaiho.gastromanager.domain.submenu.usecase;

import com.kaiho.gastromanager.domain.menu.api.MenuServicePort;
import com.kaiho.gastromanager.domain.menu.exception.MenuAlreadyExistsException;
import com.kaiho.gastromanager.domain.menu.exception.MenuDoesNotExistException;
import com.kaiho.gastromanager.domain.submenu.api.SubmenuServicePort;
import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import com.kaiho.gastromanager.domain.submenu.exception.SubmenuAlreadyExistsException;
import com.kaiho.gastromanager.domain.submenu.exception.SubmenuDoesNotExistException;
import com.kaiho.gastromanager.domain.submenu.spi.SubmenuPersistencePort;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
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

@Service
@RequiredArgsConstructor
public class SubmenuUseCase implements SubmenuServicePort {

    private final MenuServicePort menuServicePort;
    private final SubmenuPersistencePort submenuPersistencePort;

    @Override
    @Transactional
    public UUID createSubmenu(Submenu submenu) {
        validateMenuExists(submenu.getMenu().getUuid());
        if (submenuPersistencePort.submenuExistsByName(submenu.getName())) {
            throw new MenuAlreadyExistsException(submenu.getName());
        }
        return submenuPersistencePort.createSubmenu(submenu);
    }

    @Override
    public Page<Submenu> getAllSubmenus(SubmenuSearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.sortDirection().equalsIgnoreCase("desc") ? DESC : ASC,
                criteria.sortBy()
        );
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
        return submenuPersistencePort.getAllSubmenus(criteria, pageable);
    }

    @Override
    public Submenu getSubmenuById(UUID uuid) {
        return submenuPersistencePort.getSubmenuByUuid(uuid)
                                     .orElseThrow(() -> new SubmenuDoesNotExistException(uuid));
    }

    @Override
    public Submenu updateSubmenu(UUID uuid, Submenu submenu) {
        Submenu submenuById = submenuPersistencePort.getSubmenuByUuid(uuid)
                                                    .orElseThrow(() -> new SubmenuDoesNotExistException(uuid));
        if (!submenuById.getName().equals(submenu.getName()) &&
                submenuPersistencePort.submenuExistsByName(submenu.getName())) {
            throw new SubmenuAlreadyExistsException(submenu.getName());
        }
        return submenuPersistencePort.updateSubmenu(uuid, submenu);
    }

    private void validateMenuExists(UUID menuUuid) {
        if (!menuServicePort.existsById(menuUuid)) {
            throw new MenuDoesNotExistException(String.valueOf(menuUuid));
        }
    }
}
