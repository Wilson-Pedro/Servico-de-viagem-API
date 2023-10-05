package com.wamk.uber.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.IntegrationTest;
import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.templates.CarroDTOTemplate;
import com.wamk.uber.templates.CarroTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

class CarroControllerTest extends IntegrationTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @BeforeEach
    public void setUp() {
        mockMVC = webAppContextSetup(webAppContext).build();
    }

    @Test
    void deveSalvar() throws Exception {
        final CarroDTO dto = CarroDTOTemplate.valido();
        mockMVC
                .perform(post("/carros")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.ano").value(dto.getAno()))
                .andExpect(jsonPath("$.modelo").value(dto.getModelo()))
                .andExpect(jsonPath("$.placa").value(dto.getPlaca()));
    }

    @Test
    void naoDeveSalvarQuandoCarroJaExistir() throws Exception {
        final Carro carro = carroRepository.save(CarroTemplate.valido());
        final CarroDTO dto = new CarroDTO(carro);
        mockMVC
                .perform(post("/carros")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE));

    }

    @Test
    void deveBuscarPorId() throws Exception {
        final Carro carro = carroRepository.save(CarroTemplate.valido());
        mockMVC
                .perform(get(String.format("/carros/%d", carro.getId())).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carro.getId()))
                .andExpect(jsonPath("$.ano").value(carro.getAno()))
                .andExpect(jsonPath("$.modelo").value(carro.getModelo()))
                .andExpect(jsonPath("$.placa").value(carro.getPlaca()));
    }
}
