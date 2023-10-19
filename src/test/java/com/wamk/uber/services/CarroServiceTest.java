package com.wamk.uber.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.provider.CarroEntityAndCarroDtoProviderTest;
import com.wamk.uber.provider.CarrosProviderTest;
import com.wamk.uber.repositories.CarroRepository;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {
	
	private final CarroRepository carroRepository = mock(CarroRepository.class);
	
	private final CarroService carroService = new CarroService(carroRepository);
	
	private final List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207"),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
			new Carro(3L, "Forger", 2022, "FTG-0160")
	);
	
	@Test
	void deveSalvarCarroComSucesso_usando_variavel_de_classe() {
		
		final var carroEsperado = carros.get(0);
		final var carroDTO = new CarroDTO(carros.get(0));
		
		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);
		
		final var carroSalvo = carroService.save(carroDTO);
		
		assertThat(carroSalvo).usingRecursiveComparison().isEqualTo(carroEsperado);
	}
	
	@ParameterizedTest
	@ArgumentsSource(CarroEntityAndCarroDtoProviderTest.class)
	void deveSalvarCarroComSucesso_usando_teste_com_parametros(final CarroDTO carroDTO, 
			final Carro carroEsperado) {
		
		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);
		
		final var carroSalvo = carroService.save(carroDTO);
		
		assertThat(carroSalvo).usingRecursiveComparison().isEqualTo(carroEsperado);
	}
	
	@Test
	void deveBuscarTodosOsCarrosComSucesso_usando_variavel_de_classe() {
		when(carroRepository.findAll()).thenReturn(carros);
		
		final var cars = carroService.findAll();
		
		assertThat(carros).usingRecursiveComparison().isEqualTo(cars);
	}

	@ParameterizedTest
	@ArgumentsSource(CarrosProviderTest.class)
	void deveBuscarTodosOsCarrosComSucesso_usando_teste_com_parametros(
			List<Carro> carrosEsperados) {

		when(carroRepository.findAll()).thenReturn(carrosEsperados);
		
		final var cars = carroService.findAll();
		
		assertThat(cars).usingRecursiveComparison().isEqualTo(carrosEsperados);
	}
	
	@Test
	void deveBuscarCarroPorIdComSucesso() {
		
		final var carroEsperado = carros.get(0);
		
		when(carroRepository.findById(carroEsperado.getId())).thenReturn(Optional.of(carroEsperado));
		
		final var car = carroService.findById(carroEsperado.getId());
		
		assertThat(car).usingRecursiveAssertion().isEqualTo(carroEsperado);
	}
	
	@Test
	void deveAtualizarCarroComSucesso() {
		
		final var carroEsperado = carros.get(0);
		
		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);
		
		assertNotEquals("Toyota", carroEsperado.getModelo());
		
		carroEsperado.setModelo("Toyota");
		CarroDTO carroDTO = new CarroDTO(carroEsperado);
		final var carroAtualizado = carroService.save(carroDTO);
		
		assertEquals("Toyota", carroAtualizado.getModelo());
	}

	@Test
	void deveDeletarCarroComSucesso() {
		
		final var carroEsperado = carros.get(0);
		
		when(carroRepository.findById(carroEsperado.getId())).thenReturn(Optional.of(carroEsperado));
		
		doNothing().when(carroRepository).delete(carroEsperado);
		
		carroRepository.delete(carroEsperado);
		
		verify(carroRepository, times(1)).delete(carroEsperado);
	}
}