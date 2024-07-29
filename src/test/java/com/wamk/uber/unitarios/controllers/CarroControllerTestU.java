package com.wamk.uber.unitarios.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.wamk.uber.dtos.input.CarroMinDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.services.interfaces.CarroService;
import com.wamk.uber.web.controllers.CarroController;

class CarroControllerTestU {

	@InjectMocks
	CarroController carroController;
	
	CarroService carroService = mock(CarroService.class);
	
	List<Motorista> motoristas = List.of(
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO)
	);
	
	List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207",motoristas.get(0)),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460",motoristas.get(1)),
			new Carro(3L, "Forger", 2022, "FTG-0160",motoristas.get(2))
	);
	
	@Test
	void deveBuscarTodosOsCarrosComSucesso() throws Exception {
		
		final var carrosEsperados = carros;
		
		when(carroService.findAll()).thenReturn(carrosEsperados);
		
		final var cars= carroService.findAll();
		
		assertThat(cars).usingRecursiveComparison().isEqualTo(carrosEsperados);
		verify(carroService).findAll();
	}
	
	@Test
	void deveBuscarCarroPorIdComSucesso() throws Exception {
		
		final var carroEsperado = carros.get(0);
		Long id = carroEsperado.getId();
		
		when(this.carroService.findById(id)).thenReturn(carroEsperado);
		
		final var car = carroService.findById(id);
		
		assertThat(car).usingRecursiveComparison().isEqualTo(carroEsperado);
		verify(carroService).findById(id);
	}
	
	@Test
	void deveSalvarCarroComSucesso() throws Exception {
		
		final var carroEsperado = carros.get(0);
		final var carroInputDto = new CarroMinDTO(carroEsperado);
		
		when(this.carroService.save(carroInputDto)).thenReturn(carroEsperado);
		
		final var carSaved = this.carroService.save(carroInputDto);
		
		assertThat(carSaved).usingRecursiveComparison().isEqualTo(carroEsperado);
		verify(carroService).save(carroInputDto);
	}
	
//	@Test
//	void deveAtualizarCarroComSucesso() throws Exception {
//		
//		final var carroEsperado = carros.get(0);
//		CarroDTO carDto = new CarroDTO(carroEsperado);
//		
//		Long id = carroEsperado.getId();
//		
//		when(this.carroService.save(carDto)).thenReturn(carroEsperado);
//		
//		assertNotEquals("Toyota", carroEsperado.getModelo());
//		
//		carroEsperado.setModelo("Toyota");
//		
//		CarroDTO carDtoAtualizado = new CarroDTO(carroEsperado);
//		
//		when(this.carroService.save(carDtoAtualizado)).thenReturn(carroEsperado);
//		
//		final var carUpdated = this.carroService.save(carDtoAtualizado);
//		
//		assertEquals("Toyota", carUpdated.getModelo());
//		verify(this.carroService).save(carDtoAtualizado);
//	}
	
	@Test
	void deveDeletarCarroComApartirDoIdSucesso() throws Exception {
		
		final var carroEsperado = carros.get(1);
		final var id = carroEsperado.getId();
		
		doNothing().when(this.carroService).delete(id);
		
		this.carroService.delete(id);
		verify(this.carroService).delete(id);
	}
}
