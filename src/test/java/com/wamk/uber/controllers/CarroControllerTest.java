package com.wamk.uber.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.wamk.uber.entities.Carro;
import com.wamk.uber.services.CarroService;

@SpringBootTest
@AutoConfigureMockMvc
class CarroControllerTest {

	@InjectMocks
	CarroController carroController;
	
	@Autowired
	MockMvc mockMvc;
	
	CarroService carroService = mock(CarroService.class);
	
	private final List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207"),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
			new Carro(3L, "Forger", 2022, "FTG-0160")
	);
	
	@Test
	void deveBuscarTodosOsCarrosComSucesso() throws Exception {
		
		when(carroService.findAll()).thenReturn(carros);
		
		final var cars = carroService.findAll();
		
		mockMvc.perform(get("/carros")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn();
		
		assertThat(cars).usingRecursiveComparison().isEqualTo(carros);
		verify(carroService, times(1)).findAll();
	}

}
