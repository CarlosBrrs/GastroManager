package com.kaiho.gastromanager.domain.submenu.spi;

import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SubmenuPersistencePort {
    boolean submenuExistsByName(String name);

    UUID createSubmenu(Submenu submenu);

    Page<Submenu> getAllSubmenus(SubmenuSearchCriteria criteria, Pageable pageable);

    Optional<Submenu> getSubmenuByUuid(UUID uuid);

    Submenu updateSubmenu(UUID uuid, Submenu submenu);
}
