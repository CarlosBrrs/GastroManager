package com.kaiho.gastromanager.domain.submenu.api;

import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface SubmenuServicePort {
    UUID createSubmenu(Submenu submenu);

    Page<Submenu> getAllSubmenus(SubmenuSearchCriteria criteria);

    Submenu getSubmenuById(UUID submenuUuid);

    Submenu updateSubmenu(UUID submenuUuid, Submenu submenu);
}
