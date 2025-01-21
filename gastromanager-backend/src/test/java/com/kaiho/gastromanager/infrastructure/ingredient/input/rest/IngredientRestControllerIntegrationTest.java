/*
package com.kaiho.gastromanager.infrastructure.ingredient.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Ingredient API endpoints")
@Tag("integration")
class IngredientRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    String token;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        LoginRequestDto.builder()
                                .username("cbarrios")
                                .password("cbarrios").build())
                )
        );

        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String stringResponse = mvcResult.getResponse().getContentAsString();

        JSONObject response = new JSONObject(stringResponse);
        this.token = "Bearer " + response.getString("data");
        System.out.println(this.token);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllIngredients() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/ingredients").header("Authorization", this.token).accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true));
    }

    @Test
    void getIngredientById() {
    }

    @Test
    void addIngredient() {
    }

    @Test
    void updateIngredient() {
    }
}*/
