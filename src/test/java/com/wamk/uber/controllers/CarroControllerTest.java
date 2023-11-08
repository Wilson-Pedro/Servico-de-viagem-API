package com.wamk.uber.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.services.CarroService;

@SpringBootTest
@AutoConfigureMockMvc
class CarroControllerTest {

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
		
		final var carsExpected = carros;
		
		when(carroService.findAll()).thenReturn(carsExpected);
		
		mockMvc.perform(get("/carros"))
				.andExpect(status().isOk())
				.andReturn();
		
		final var cars= carroService.findAll();
		
		assertThat(cars).usingRecursiveComparison().isEqualTo(carsExpected);
		verify(carroService, times(1)).findAll();
	}
	
	@Test
	void deveBuscarCarroPorIdComSucesso() throws Exception {
		
		final var carExpected = carros.get(0);
		Long id = carExpected.getId();
		
		when(this.carroService.findById(id)).thenReturn(carExpected);
		
		mockMvc.perform(get("/carros/{id}", 1L))
				.andExpect(status().isOk())
				.andReturn();
		
		final var car = carroService.findById(id);
		
		assertThat(car).usingRecursiveComparison().isEqualTo(carExpected);
		verify(carroService, times(1)).findById(id);
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
		
		final var carExpected = new Carro(4L, "Uno", 2022, "JVF-9297");
		final var carroDto = new CarroDTO(carExpected);
		
		when(this.carroService.save(carroDto)).thenReturn(carExpected);
		
		String jsonRequest = objectMapper.writeValueAsString(carroDto);
		
		mockMvc.perform(post("/carros/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andReturn();
		
		final var carSaved = this.carroService.save(carroDto);
		
		assertThat(carSaved).usingRecursiveComparison().isEqualTo(carExpected);
		verify(carroService, times(1)).save(carroDto);
	}
	
	@Test
	void deveAtualizarCarroComSucesso() throws Exception {
		
		final var carExpected = carros.get(0);
		CarroDTO carDto = new CarroDTO(carExpected);
		
		Long id = carExpected.getId();
		
		when(this.carroService.save(carDto)).thenReturn(carExpected);
		
		assertNotEquals("Toyota", carExpected.getModelo());
		
		carExpected.setModelo("Toyota");
		
		CarroDTO carDtoUpdated = new CarroDTO(carExpected);
		
		String jsonRequest = this.objectMapper.writeValueAsString(carDtoUpdated);
		
		mockMvc.perform(put("/carros/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		when(this.carroService.save(carDtoUpdated)).thenReturn(carExpected);
		
		final var carUpdated = this.carroService.save(carDtoUpdated);
		
		assertEquals("Toyota", carUpdated.getModelo());
		verify(this.carroService, times(1)).save(carDtoUpdated);
	}
	
//	@Test
//	void deveDeletarCarroComApartirDoIdSucesso() throws Exception {
//		
//		final var carExpected = carros.get(0);
//		Long id = carExpected.getId();
//		
//		when(this.carroService.findById(id)).thenReturn(carExpected);
//		
//		final var car = carroService.findById(id);
//		
//		doNothing().when(this.carroService).delete(car.getId());
//		
//		mockMvc.perform(delete("/carros/{id}", 1L))
//			.andExpect(status().isNoContent())
//			.andReturn();
//		
//		this.carroService.delete(car.getId());
//		
//		verify(carroService, times(1)).delete(id);
//	}
}
