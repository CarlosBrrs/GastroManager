package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.adapter;

import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.spi.IngredientPersistencePort;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper.IngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class IngredientEntityAdapter implements IngredientPersistencePort {

    private final IngredientEntityRepository ingredientEntityRepository;
    private final IngredientEntityMapper ingredientEntityMapper;


    @Override
    public List<Ingredient> getAllIngredients() {
        List<IngredientEntity> entityList = ingredientEntityRepository.findAll();
        return entityList.stream().map(ingredientEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Ingredient> getIngredientById(UUID uuid) {
        return ingredientEntityRepository.findById(uuid).map(ingredientEntityMapper::toDomain);
    }
}
