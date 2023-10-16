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

import com.wamk.uber.provider.CarroEntityAndCarroDtoProviderTest;
import com.wamk.uber.provider.CarrosProviderTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.repositories.CarroRepository;

class CarroServiceTest {

	private final CarroRepository carroRepository = mock(CarroRepository.class);
	private final CarroService service = new CarroService(this.carroRepository);

	private final List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207"),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
			new Carro(3L, "Forger", 2022, "FTG-0160")
	);

	@Test
	void deveSalvarCarroComSucesso_usando_variavel_da_classe() {
		// DADO que recebamos um carro no formato DTO
		final var carroEsperado = carros.get(0);
		final var carroDTO = new CarroDTO(carros.get(0));

		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);

		// QUANDO salvarmos o carro na base de dados, deve retornar o carro salvo no formato DTO.
		final var carroSalvo = service.save(carroDTO);

		// ENTAO verifique se os campos do objeto carroSalvo são iguais ao objeto carroEsperado
		assertThat(carroSalvo)
				.usingRecursiveComparison()
				.isEqualTo(carroEsperado);

		// O CÓDIGO ACIMA é equivalente ao CÓDIGO ABAIXO - use um dos dois
		assertThat(carroSalvo.getId()).isEqualTo(carroEsperado.getId());
		assertThat(carroSalvo.getModelo()).isEqualTo(carroEsperado.getModelo());
		assertThat(carroSalvo.getAno()).isEqualTo(carroEsperado.getAno());
		assertThat(carroSalvo.getPlaca()).isEqualTo(carroEsperado.getPlaca());
	}

	@ParameterizedTest
	@ArgumentsSource(CarroEntityAndCarroDtoProviderTest.class)
	void deveSalvarCarroComSucesso_usando_teste_com_parametros(final CarroDTO carroDTO,
															   final Carro carroEsperado) {
		// DADO que recebamos um carro no formato DTO

		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);

		// QUANDO salvarmos o carro na base de dados, deve retornar o carro salvo no formato DTO.
		final var carroSalvo = service.save(carroDTO);

		// ENTAO verifique se os campos do objeto carroSalvo são iguais ao objeto carroEsperado
		assertThat(carroSalvo)
				.usingRecursiveComparison()
				.isEqualTo(carroEsperado);

		// O CÓDIGO ACIMA é equivalente ao CÓDIGO ABAIXO - use um dos dois
		assertThat(carroSalvo.getId()).isEqualTo(carroEsperado.getId());
		assertThat(carroSalvo.getModelo()).isEqualTo(carroEsperado.getModelo());
		assertThat(carroSalvo.getAno()).isEqualTo(carroEsperado.getAno());
		assertThat(carroSalvo.getPlaca()).isEqualTo(carroEsperado.getPlaca());
	}

	@Test
	void deveBuscarTodosOsCarrosComSucesso_usando_variavel_da_classe() {
		// DADO que invocamos o método findAll do repositório, esperamos que retorne uma lista de carros contidos na base de dados
		when(carroRepository.findAll()).thenReturn(carros);

		// QUANDO buscamos todos os carros, deve retornar uma lista com todos os carros contidos na base de dados
		final var cars = service.findAll();

		// ENTAO verifique se os carros retornados são os mesmos que os carros esperados
		assertThat(cars).usingRecursiveComparison().isEqualTo(carros);
	}

	@ParameterizedTest
	@ArgumentsSource(CarrosProviderTest.class)
	void deveBuscarTodosOsCarrosComSucesso_usando_teste_com_parametro(final List<Carro> carrosEsperados) {
		// DADO que invocamos o método findAll do repositório, esperamos que retorne uma lista de carros contidos na base de dados
		when(carroRepository.findAll()).thenReturn(carrosEsperados);

		// QUANDO buscamos todos os carros, deve retornar uma lista com todos os carros contidos na base de dados
		final var cars = service.findAll();

		// ENTAO verifique se os carros retornados são os mesmos que os carros esperados
		assertThat(cars).usingRecursiveComparison().isEqualTo(carrosEsperados);
	}

	@Test
	void deveBuscarCarroPorIdComSucesso() {
		// DADO que invocamos o método findById do repositório passando um ID que exista no banco de dados
		// deve retornar um carro contido na base de dados com o ID informado

		final var carroEsperado = carros.get(0);

		when(carroRepository.findById(carroEsperado.getId())).thenReturn(Optional.of(carroEsperado));

		// QUANDO buscamos um carro pelo ID no service, deve retornar um carro contido na base de dados com o ID informado
		final var car = service.findById(carroEsperado.getId());

		// ENTAO verifique se o carro retornado é o mesmo que o carro esperado
		assertThat(car).usingRecursiveComparison().isEqualTo(carroEsperado);
	}

	@Test
	void deveAtualizarCarroComSucesso() {
		// DADO que o um carro está no repositório e que seu modelo será atualizado para "Toyota"
		final var carroEsperado = carros.get(0);

		when(carroRepository.save(carroEsperado)).thenReturn(carroEsperado);

		//garantindo que o carro não é Toyota
        assertNotEquals("Toyota", carroEsperado.getModelo());

		carroEsperado.setModelo("Toyota");
		CarroDTO carroDTO = new CarroDTO(carroEsperado);

		// QUANDO salvamos o carro no service, deve retornar o carro atualizado
		final var carroAtualizado = service.save(carroDTO);

		// ENTAO verifique se o modelo do carro atualizado é "Toyota"
		assertEquals("Toyota", carroAtualizado.getModelo());
	}

	@Test
	void deveDeletarCarroComSucesso() {
		// DADO que o carro esperado existe no repositório
		final var carroEsperado = carros.get(0);
		when(carroRepository.findById(carroEsperado.getId())).thenReturn(Optional.of(carroEsperado));

		//testando método VOID
		doNothing().when(carroRepository).delete(carroEsperado);

		// QUANDO deletamos o carro Fiat
		this.carroRepository.delete(carroEsperado);

		// ENTAO verifique se o método 'delete' do repositório foi chamado apenas 1 vez
		verify(carroRepository, times(1)).delete(carroEsperado);
	}
}
