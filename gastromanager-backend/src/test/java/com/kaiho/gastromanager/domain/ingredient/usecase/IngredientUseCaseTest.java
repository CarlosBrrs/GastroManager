package com.kaiho.gastromanager.domain.ingredient.usecase;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.spi.IngredientPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngredientUseCaseTest {

    @InjectMocks
    IngredientUseCase underTest;

    @Mock
    IngredientPersistencePort ingredientPersistencePort;

    @Test
    void testFindAllIngredientsSuccess() {
        //given
        List<Ingredient> list = List.of(Ingredient.builder().name("1").build(), Ingredient.builder().name("2").build());
        given(ingredientPersistencePort.getAllIngredients()).willReturn(list);

        //when
        List<Ingredient> allIngredients = underTest.getAllIngredients();

        //then
        assertThat(allIngredients).hasSameSizeAs(list);
        verify(ingredientPersistencePort, times(1)).getAllIngredients();
    }

    @Test
    void testFindAllIngredientsEmptyListSuccess() {
        //given
        List<Ingredient> list = List.of();
        given(ingredientPersistencePort.getAllIngredients()).willReturn(list);

        //when
        List<Ingredient> allIngredients = underTest.getAllIngredients();

        //then
        assertThat(allIngredients).hasSameSizeAs(list);
        verify(ingredientPersistencePort, times(1)).getAllIngredients();
    }

    @Test
    void testFindIngredientByIdSuccess() {
        //given
        UUID uuid = UUID.randomUUID();
        Ingredient ingredient = Ingredient.builder().uuid(uuid).name("1").build();
        given(ingredientPersistencePort.getIngredientById(uuid)).willReturn(Optional.of(ingredient));

        //when
        Ingredient ingredientById = underTest.getIngredientById(uuid);

        //then
        assertThat(ingredientById).isEqualTo(ingredient);
        verify(ingredientPersistencePort, times(1)).getIngredientById(uuid);
    }

    @Test
    void testFindIngredientByIdThrowIngredientDoesNotExistException() {
        //given
        UUID uuid = UUID.randomUUID();
        given(ingredientPersistencePort.getIngredientById(any(UUID.class))).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(
                () -> underTest.getIngredientById(uuid)
        );

        //then
        assertThat(thrown).isInstanceOf(IngredientDoesNotExistException.class)
                .hasMessage("Ingredient with UUID: " + uuid + " does not exist");
        verify(ingredientPersistencePort, times(1)).getIngredientById(uuid);
    }
}