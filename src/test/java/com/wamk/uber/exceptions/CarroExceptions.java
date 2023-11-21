package com.wamk.uber.exceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.services.CarroService;

@SpringBootTest
class CarroExceptions {
	
	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	CarroService carroService;
	
	private final List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207"),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
			new Carro(3L, "Forger", 2022, "FTG-0160")
	);
	
	@Test
	@DisplayName("Deve lançar exceção: EntidadeNaoEncontradaException")
	void deveLancarExcecaoAposTentarBuscarUmCarroInexistente() {
		
		Long id = 4L;
		
		assertThrows(EntidadeNaoEncontradaException.class, 
				() -> this.carroService.findById(id));
	}

	@Test
	@DisplayName("Deve lançar a Exceção: PlacaExistenteException")
	void deveLancarExcecaoAposTentarRegistrarUmCarro() {
		
		final var carroDto = new CarroDTO(carros.get(0));
		
		assertThrows(PlacaExistenteException.class, () -> this.carroService.save(carroDto));
	}
	
	@Test
	@DisplayName("Deve lançar a Exceção: PlacaExistenteException")
	void deveLancarExcecaoTentarAtualizarUmCarro() {
		
		final var carroDto = new CarroDTO(carros.get(0));
		Long id = 2L;
		
		assertThrows(PlacaExistenteException.class, 
				() -> this.carroService.atualizarCadastro(carroDto, id));
	}
}
