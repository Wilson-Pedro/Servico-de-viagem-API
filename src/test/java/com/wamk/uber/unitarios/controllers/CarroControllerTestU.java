package com.wamk.uber.unitarios.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.controllers.CarroController;
import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.services.CarroService;

@SpringBootTest
@AutoConfigureMockMvc
class CarroControllerTestU {

	@InjectMocks
	CarroController carroController;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	CarroService carroService = mock(CarroService.class);
	
	private final List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207"),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
			new Carro(3L, "Forger", 2022, "FTG-0160")
	);
	
	@Test
	void deveBuscarTodosOsCarrosComSucesso() throws Exception {
		
		final var carrosEsperados = carros;
		
		when(carroService.findAll()).thenReturn(carrosEsperados);
		
		mockMvc.perform(get("/carros"))
				.andExpect(status().isOk())
				.andReturn();
		
		final var cars= carroService.findAll();
		
		assertThat(cars).usingRecursiveComparison().isEqualTo(carrosEsperados);
		verify(carroService).findAll();
	}
	
	@Test
	void deveBuscarCarroPorIdComSucesso() throws Exception {
		
		final var carroEsperado = carros.get(0);
		Long id = carroEsperado.getId();
		
		when(this.carroService.findById(id)).thenReturn(carroEsperado);
		
		mockMvc.perform(get("/carros/{id}", 1L))
				.andExpect(status().isOk())
				.andReturn();
		
		final var car = carroService.findById(id);
		
		assertThat(car).usingRecursiveComparison().isEqualTo(carroEsperado);
		verify(carroService).findById(id);
	}
	
//	@Test
//	void devePaginarAListaDeCarros() throws Exception {
//		Pageable pageable = PageRequest.of(0, 10);
//		Page<Carro> carrosPages = (Page<Carro>) carros;
//		when(this.carroService.findAll(pageable)).thenReturn(carrosPages);
//		
//		Page<Carro> pages = carroService.findAll(pageable);
//		
//		mockMvc.perform(post("/carros/pages"))
//				.andExpect(status().isCreated())
//				.andReturn();
//		
//		assertThat(pages).usingRecursiveComparison().isEqualTo(carrosPages);
//	}
	
	@Test
	void deveSalvarCarroComSucesso() throws Exception {
		
		final var carroEsperado = new Carro(4L, "Uno", 2022, "JVF-9297");
		final var carroDto = new CarroDTO(carroEsperado);
		
		when(this.carroService.save(carroDto)).thenReturn(carroEsperado);
		
		String jsonRequest = objectMapper.writeValueAsString(carroDto);
		
		mockMvc.perform(post("/carros/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andReturn();
		
		final var carSaved = this.carroService.save(carroDto);
		
		assertThat(carSaved).usingRecursiveComparison().isEqualTo(carroEsperado);
		verify(carroService).save(carroDto);
	}
	
	@Test
	void deveAtualizarCarroComSucesso() throws Exception {
		
		final var carroEsperado = carros.get(0);
		CarroDTO carDto = new CarroDTO(carroEsperado);
		
		Long id = carroEsperado.getId();
		
		when(this.carroService.save(carDto)).thenReturn(carroEsperado);
		
		assertNotEquals("Toyota", carroEsperado.getModelo());
		
		carroEsperado.setModelo("Toyota");
		
		CarroDTO carDtoAtualizado = new CarroDTO(carroEsperado);
		
		String jsonRequest = this.objectMapper.writeValueAsString(carDtoAtualizado);
		
		mockMvc.perform(put("/carros/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		when(this.carroService.save(carDtoAtualizado)).thenReturn(carroEsperado);
		
		final var carUpdated = this.carroService.save(carDtoAtualizado);
		
		assertEquals("Toyota", carUpdated.getModelo());
		verify(this.carroService).save(carDtoAtualizado);
	}
	
	@Test
	void deveDeletarCarroComApartirDoIdSucesso() throws Exception {
		
		final var carroEsperado = carros.get(1);
		final var id = carroEsperado.getId();
		
		doNothing().when(this.carroService).delete(id);
		
		mockMvc.perform(delete("/carros/{id}", id))
				.andExpect(status().isNoContent())
				.andReturn();
		
		this.carroService.delete(id);
		verify(this.carroService).delete(id);
	}
}
