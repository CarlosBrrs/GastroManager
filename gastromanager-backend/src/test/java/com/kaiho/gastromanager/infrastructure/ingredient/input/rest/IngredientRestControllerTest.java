package com.kaiho.gastromanager.infrastructure.ingredient.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaiho.gastromanager.application.ingredient.dto.request.IngredientRequestDto;
import com.kaiho.gastromanager.application.ingredient.dto.response.IngredientResponseDto;
import com.kaiho.gastromanager.application.ingredient.handler.IngredientHandler;
import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class IngredientRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IngredientHandler ingredientHandler;

    List<IngredientResponseDto> ingredientsResponseList;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

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

        this.mockMvc.perform(get(this.baseUrl + "/ingredients").accept(MediaType.APPLICATION_JSON))
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

        this.mockMvc.perform(get(this.baseUrl + "/ingredients").accept(MediaType.APPLICATION_JSON))
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

        this.mockMvc.perform(get(this.baseUrl + "/ingredients/" + ingredientResponseDto.uuid())
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

        this.mockMvc.perform(get(this.baseUrl + "/ingredients/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Ingredient with UUID: " + uuid + " does not exist"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddIngredientSuccess() throws Exception {
        IngredientRequestDto requestDto = IngredientRequestDto.builder()
                .name("Oil")
                .stockLevel(3000)
                .minimumStockLevel(500)
                .updateReason("New ingredient")
                .unit("MILLILITRES")
                .pricePerUnit(0.006)
                .build();
        UUID uuid = UUID.randomUUID();
        given(ingredientHandler.addIngredient(any(IngredientRequestDto.class))).willReturn(
                buildSuccessResponse("Ingredient added successfully", uuid)
        );

        this.mockMvc.perform(post(this.baseUrl + "/ingredients")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Ingredient added successfully"))
                .andExpect(jsonPath("$.data").value(uuid.toString()));
    }

    @Test
    void testUpdateIngredientSuccess() throws Exception {
        UUID uuid = UUID.randomUUID();
        IngredientRequestDto requestDto = IngredientRequestDto.builder()
                .name("Oil")
                .stockLevel(3000)
                .minimumStockLevel(500)
                .updateReason("New ingredient")
                .unit("MILLILITRES")
                .pricePerUnit(0.006)
                .build();

        IngredientResponseDto updated = IngredientResponseDto.builder()
                .uuid(uuid)
                .name("Oil")
                .stockLevel(3000)
                .unit("MILLILITRES")
                .pricePerUnit(0.006)
                .lastUpdated(Instant.now())
                .build();

        given(ingredientHandler.updateIngredient(any(UUID.class),any(IngredientRequestDto.class))).willReturn(
                buildSuccessResponse("Ingredient updated successfully", updated)
        );

        this.mockMvc.perform(put(this.baseUrl + "/ingredients/" + uuid)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Ingredient updated successfully"))
                .andExpect(jsonPath("$.data.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.data.name").value(updated.name()))
                .andExpect(jsonPath("$.data.stockLevel").value(updated.stockLevel()))
                .andExpect(jsonPath("$.data.unit").value(updated.unit()))
                .andExpect(jsonPath("$.data.pricePerUnit").value(updated.pricePerUnit()))
                .andExpect(jsonPath("$.data.lastUpdated").value(updated.lastUpdated().toString()));
    }

    @Test
    void testUpdateIngredientNonExistingUuidException() throws Exception {
        UUID uuid = UUID.randomUUID();
        IngredientRequestDto requestDto = IngredientRequestDto.builder()
                .name("Oil")
                .stockLevel(3000)
                .minimumStockLevel(500)
                .updateReason("New ingredient")
                .unit("MILLILITRES")
                .pricePerUnit(0.006)
                .build();

        given(ingredientHandler.updateIngredient(any(UUID.class),any(IngredientRequestDto.class))).willThrow(
                new IngredientDoesNotExistException(uuid.toString()));

        this.mockMvc.perform(put(this.baseUrl + "/ingredients/" + uuid)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Ingredient with UUID: " + uuid + " does not exist"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}