package com.kaiho.gastromanager.domain.ingredient.usecase;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistExceptionException;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kaiho.gastromanager.domain.ingredient.model.Unit.UNITS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        given(ingredientPersistencePort.getIngredientByUuid(uuid)).willReturn(Optional.of(ingredient));

        //when
        Ingredient ingredientById = underTest.getIngredientById(uuid);

        //then
        assertThat(ingredientById).isEqualTo(ingredient);
        verify(ingredientPersistencePort, times(1)).getIngredientByUuid(uuid);
    }

    @Test
    void testFindIngredientByIdThrowIngredientDoesNotExistException() {
        //given
        UUID uuid = UUID.randomUUID();
        given(ingredientPersistencePort.getIngredientByUuid(any(UUID.class))).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(
                () -> underTest.getIngredientById(uuid)
        );

        //then
        assertThat(thrown).isInstanceOf(IngredientDoesNotExistExceptionException.class)
                .hasMessage("Ingredient with UUID: " + uuid + " does not exist");
        verify(ingredientPersistencePort, times(1)).getIngredientByUuid(uuid);
    }

    @Test
    void testCreateIngredientSuccess() {
        //given
        UUID uuid = UUID.randomUUID();
        Ingredient ingredient = Ingredient.builder().name("eggs").build();
        given(ingredientPersistencePort.addIngredient(any(Ingredient.class))).willReturn(uuid);

        //when
        UUID savedUuid = underTest.addIngredient(ingredient);
        //then
        assertThat(savedUuid).isEqualTo(uuid);
        verify(ingredientPersistencePort, times(1)).addIngredient(ingredient);
    }

    @Test
    void testUpdateIngredientSuccess() {
        //given
        UUID uuid = UUID.randomUUID();
        Ingredient oldIngredient = Ingredient.builder()
                .uuid(uuid)
                .name("eggs")
                .unit(UNITS)
                .availableStock(300)
                .pricePerUnit(0.2)
                .minimumStockQuantity(20)
                .supplier("Eggs supplier")
                .build();

        Ingredient updatedIngredient = Ingredient.builder()
                .uuid(uuid)
                .name("eggs")
                .unit(UNITS)
                .availableStock(9500)
                .pricePerUnit(0.18)
                .minimumStockQuantity(30)
                .supplier("Eggs supplier")
                .build();

        given(ingredientPersistencePort.getIngredientByUuid(uuid)).willReturn(Optional.of(oldIngredient));
        given(ingredientPersistencePort.updateIngredient(any(Ingredient.class))).willReturn(updatedIngredient);

        //when
        Ingredient result = underTest.updateIngredient(uuid, updatedIngredient);
        //then
        assertThat(result.uuid()).isEqualTo(updatedIngredient.uuid());
        assertThat(result.name()).isEqualTo(updatedIngredient.name());
        assertThat(result.unit()).isEqualTo(updatedIngredient.unit());
        assertThat(result.availableStock()).isEqualTo(updatedIngredient.availableStock());
        assertThat(result.pricePerUnit()).isEqualTo(updatedIngredient.pricePerUnit());
        assertThat(result.minimumStockQuantity()).isEqualTo(updatedIngredient.minimumStockQuantity());
        verify(ingredientPersistencePort, times(1)).getIngredientByUuid(uuid);
        verify(ingredientPersistencePort, times(1)).updateIngredient(updatedIngredient);
    }

    @Test
    void testUpdateIngredientNotFound() {
        //given
        UUID uuid = UUID.randomUUID();
        Ingredient updatedIngredient = Ingredient.builder()
                .uuid(uuid)
                .name("eggs")
                .unit(UNITS)
                .availableStock(9500)
                .pricePerUnit(0.18)
                .minimumStockQuantity(30)
                .supplier("Eggs supplier")
                .build();

        given(ingredientPersistencePort.getIngredientByUuid(uuid)).willReturn(Optional.empty());

        //when
        assertThrows(IngredientDoesNotExistExceptionException.class, () -> underTest.updateIngredient(uuid, updatedIngredient));

        //then

        verify(ingredientPersistencePort, times(1)).getIngredientByUuid(uuid);
        verify(ingredientPersistencePort, times(0)).updateIngredient(updatedIngredient);
    }
}