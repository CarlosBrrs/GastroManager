package com.kaiho.gastromanager.infrastructure.ingredient.input.rest;

import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.application.ingredient.handler.IngredientHandler;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class IngredientRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IngredientHandler ingredientHandler;

    List<IngredientResponseDto> ingredientsResponseList;

    @BeforeEach
    void setUp() {
        this.ingredientsResponseList = new ArrayList<>();

        IngredientResponseDto eggs = IngredientResponseDto.builder()
                .uuid(UUID.randomUUID())
                .name("eggs")
                .stockLevel(30)
                .unit(Unit.UNITS.getSymbol())
                .pricePerUnit(0.2)
                .lastUpdated(Instant.now())
                .build();
        this.ingredientsResponseList.add(eggs);

        IngredientResponseDto salt = IngredientResponseDto.builder()
                .uuid(UUID.randomUUID())
                .name("Salt")
                .stockLevel(1000)
                .unit(Unit.GRAMS.getSymbol())
                .pricePerUnit(0.0015)
                .lastUpdated(Instant.now())
                .build();
        this.ingredientsResponseList.add(salt);
    }

    @Test
    void testGetAllIngredientsSuccess() throws Exception {
        given(ingredientHandler.getAllIngredients()).willReturn(
                buildSuccessResponse("List of ingredients retrieved successfully", this.ingredientsResponseList)
        );

        this.mockMvc.perform(get("/api/v1/ingredients").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("List of ingredients retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(this.ingredientsResponseList.size()));
    }

    @Test
    void testGetAllIngredientsEmptyListSuccess() throws Exception {
        given(ingredientHandler.getAllIngredients()).willReturn(
                buildSuccessResponse("List of ingredients retrieved successfully", new ArrayList<>())
        );

        this.mockMvc.perform(get("/api/v1/ingredients").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("List of ingredients retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.size()").value(0));
    }

    @Test
    void testGetIngredientByIdSuccess() throws Exception {
        IngredientResponseDto ingredientResponseDto = this.ingredientsResponseList.get(0);
        given(ingredientHandler.getIngredientById(ingredientResponseDto.uuid())).willReturn(
                buildSuccessResponse("Ingredient retrieved successfully", ingredientResponseDto)
        );

        this.mockMvc.perform(get("/api/v1/ingredients/" + ingredientResponseDto.uuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Ingredient retrieved successfully"))
                .andExpect(jsonPath("$.data.uuid").value(ingredientResponseDto.uuid().toString()))
                .andExpect(jsonPath("$.data.name").value(ingredientResponseDto.name()))
                .andExpect(jsonPath("$.data.stockLevel").value(ingredientResponseDto.stockLevel()))
                .andExpect(jsonPath("$.data.unit").value(ingredientResponseDto.unit()))
                .andExpect(jsonPath("$.data.pricePerUnit").value(ingredientResponseDto.pricePerUnit()))
                .andExpect(jsonPath("$.data.lastUpdated").value(ingredientResponseDto.lastUpdated().toString()));
    }

    @Test
    void testGetIngredientByIdNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        given(ingredientHandler.getIngredientById(uuid)).willThrow(
                new IngredientDoesNotExistException(uuid.toString())
        );

        this.mockMvc.perform(get("/api/v1/ingredients/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Ingredient with UUID: " + uuid + " does not exist"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}