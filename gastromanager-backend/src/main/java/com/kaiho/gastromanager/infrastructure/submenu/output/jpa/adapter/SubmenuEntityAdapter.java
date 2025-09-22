package com.kaiho.gastromanager.infrastructure.submenu.output.jpa.adapter;

import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import com.kaiho.gastromanager.domain.submenu.exception.SubmenuDoesNotExistException;
import com.kaiho.gastromanager.domain.submenu.spi.SubmenuPersistencePort;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity.SubmenuEntity;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.mapper.SubmenuEntityMapper;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.repository.SubmenuEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static com.kaiho.gastromanager.infrastructure.submenu.output.jpa.specification.SubmenuEntitySpecification.buildSpecification;

@RequiredArgsConstructor
@Repository
public class SubmenuEntityAdapter implements SubmenuPersistencePort {

    private final SubmenuEntityRepository submenuEntityRepository;
    private final SubmenuEntityMapper submenuEntityMapper;


    @Override
    public boolean submenuExistsByName(String name) {
        return submenuEntityRepository.existsByName(name, getCurrentRestaurant());
    }

    @Override
    public UUID createSubmenu(Submenu submenu) {
        SubmenuEntity entity = submenuEntityMapper.toEntity(submenu);
        SubmenuEntity save = submenuEntityRepository.save(entity);
        return save.getUuid();
    }

    @Override
    public Page<Submenu> getAllSubmenus(SubmenuSearchCriteria criteria, Pageable pageable) {
        Specification<SubmenuEntity> spec = buildSpecification(criteria);
        Page<SubmenuEntity> entityList = submenuEntityRepository.findAll(spec, pageable);
        return entityList.map(submenuEntityMapper::toDomain);
    }

    @Override
    public Optional<Submenu> getSubmenuByUuid(UUID uuid) {
        UUID currentRestaurant = getCurrentRestaurant();
        return submenuEntityRepository.findByUuid(uuid, currentRestaurant)
                                      .map(submenuEntityMapper::toDomain);
    }

    @Override
    public Submenu updateSubmenu(UUID uuid, Submenu submenu) {
        SubmenuEntity existingEntity = submenuEntityRepository.findByUuid(uuid, getCurrentRestaurant())
                                                              .orElseThrow(() -> new SubmenuDoesNotExistException(uuid));

        existingEntity.setName(submenu.getName());
        existingEntity.setDescription(submenu.getDescription());

        SubmenuEntity saved = submenuEntityRepository.save(existingEntity);
        return submenuEntityMapper.toDomain(saved);
    }
}
