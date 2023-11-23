package com.wamk.uber.integracao.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.services.CarroService;

@SpringBootTest
class CarroServiceTestI {

	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	CarroService carroService;
	
	@BeforeEach
	void setup() {
		carroRepository.deleteAll();
	}
	
	@Test
	void deveSalvarCarroComSucesso() {
		Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
		CarroDTO carroDto = new CarroDTO(carro);
		
		carroService.save(carroDto);
		
		assertEquals(1, carroRepository.count());
	}
	
	@Test
	void deveSBuscarTodosOsCarrosComSucesso() {
		List<Carro> carrosEsperados = List.of(
				new Carro(1L, "Fiat", 2022, "JVF-9207"),
				new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
				new Carro(3L, "Forger", 2022, "FTG-0160")
		);
		
		carroRepository.saveAll(carrosEsperados);
		List<Carro> carros = carroService.findAll();
		
		assertThat(carros).usingRecursiveComparison().isEqualTo(carrosEsperados);
	}

	@Test
	void deveBuscarCarroAPartirDoIdComSucesso() {
		Carro carroEsperado = new Carro(1L, "Fiat", 2022, "JVF-9207");
		carroRepository.save(carroEsperado);
		
		Carro carro = carroService.findById(carroEsperado.getId());
		
		assertThat(carro).usingRecursiveComparison().isEqualTo(carroEsperado);
	}
	
	@Test
	void deveDeletarCarroComSucesso() {
		Carro carroEsperado = new Carro(1L, "Fiat", 2022, "JVF-9207");
		carroRepository.save(carroEsperado);
		
		assertEquals(1, carroRepository.count());
		
		carroService.delete(carroEsperado.getId());
		
		assertEquals(0, carroRepository.count());
	}
	
	@Test
	void deveAtualizarCarroComSucesso() {
		Carro carroEsperado = new Carro(1L, "Fiat", 2022, "JVF-9207");
		carroRepository.save(carroEsperado);
		
		carroEsperado.setModelo("Toyota");
		
		CarroDTO carroDto = new CarroDTO(carroEsperado);
		Carro carroAtualizado = carroService.atualizarCadastro(carroDto, carroDto.getId());
		
		assertEquals("Toyota", carroAtualizado.getModelo());
		
	}
}
