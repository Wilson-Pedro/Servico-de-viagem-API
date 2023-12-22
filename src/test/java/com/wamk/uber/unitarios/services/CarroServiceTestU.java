package com.wamk.uber.unitarios.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.provider.CarroEntityAndCarroDtoProviderTest;
import com.wamk.uber.provider.CarrosProviderTest;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.services.CarroService;
import com.wamk.uber.services.UsuarioService;

@ExtendWith(MockitoExtension.class)
class CarroServiceTestU {
	
	private final MyObjectMapper modelMapper = mock(MyObjectMapper.class);
	
	private final CarroRepository carroRepository = mock(CarroRepository.class);
	
	private final UsuarioService usuarioService = mock(UsuarioService.class);
	
	private final CarroService carroService = new CarroService(modelMapper, carroRepository, usuarioService);
	
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
	void deveSalvarCarroComSucesso_usandoVariavelDeClasse() {
		
		final var carroEsperado = carros.get(0);
		
		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);
		
		final var carroSalvo = carroRepository.save(carroEsperado);
		
		assertThat(carroSalvo).usingRecursiveComparison().isEqualTo(carroEsperado);
	}
	
	@ParameterizedTest
	@ArgumentsSource(CarroEntityAndCarroDtoProviderTest.class)
	void deveSalvarCarroComSucesso_usandoTesteComParametros(final CarroDTO carroDTO, 
			final Carro carroEsperado) {
		
		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);
		
		final var carroSalvo = carroRepository.save(carroEsperado);
		
		assertThat(carroSalvo).usingRecursiveComparison().isEqualTo(carroEsperado);
	}
	
	@Test
	void deveBuscarTodosOsCarrosComSucesso_usandoVariavelDeClasse() {
		when(carroRepository.findAll()).thenReturn(carros);
		
		final var cars = carroService.findAll();
		
		assertThat(carros).usingRecursiveComparison().isEqualTo(cars);
	}

	@ParameterizedTest
	@ArgumentsSource(CarrosProviderTest.class)
	void deveBuscarTodosOsCarrosComSucesso_usandoTesteComParametros(
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
		
		assertNotEquals("Toyota", carroEsperado.getModelo());
		
		carroEsperado.setModelo("Toyota");
		
		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);
		
		final var carroAtualizado = carroRepository.save(carroEsperado);
		
		assertEquals("Toyota", carroAtualizado.getModelo());
	}

	@Test
	void deveDeletarCarroComApartirDoIdSucesso() {
		
		final var carroEsperado = carros.get(0);
		
		when(carroRepository.findById(carroEsperado.getId())).thenReturn(Optional.of(carroEsperado));
		
		doNothing().when(carroRepository).delete(carroEsperado);
		
		carroRepository.delete(carroEsperado);
		
		verify(carroRepository).delete(carroEsperado);
	}
	
	@Test
	void devePaginarUmaListaDeCarrosComSucesso() {
		
		List<Carro> list = this.carros;
		Page<Carro> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());
		
		when(carroService.findAll(any(Pageable.class))).thenReturn(page);
		
		Page<Carro> pageCarro = carroService.findAll(PageRequest.of(0, 10));
		
		assertNotNull(pageCarro);
		assertEquals(pageCarro.getContent().size(), list.size());
	} 
}