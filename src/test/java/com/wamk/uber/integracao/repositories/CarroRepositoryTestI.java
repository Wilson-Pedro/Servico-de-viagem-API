package com.wamk.uber.integracao.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.repositories.CarroRepository;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarroRepositoryTestI {
	
	@Autowired
	CarroRepository carroRepository;
	
	@Test
	@Order(1)
	void saveCar() {
		carroRepository.save(new Carro(null, "Toyota", 2022, "JHG-9117", null));
	}

	@Test
	@DisplayName("Deve Buscar Carro a partir de uma plca que existe com sucesso")
	void deveBuscarCarroComSucessoCase1() {
		carroRepository.save(new Carro(null, "Toyota", 2022, "JFJ-9117", null));
		
		Optional<Carro> result = this.carroRepository.findCarroByPlaca("JFJ-9117");
		
		assertThat(result.isPresent());
	}
	
	@Test
	@DisplayName("Não deve Buscar Carro com sucesso a partir de uma placa que não existe")
	void deveBuscarCarroComSucessoCase2() {
		String placa = "JFR-9337";
		
		Optional<Carro> result = this.carroRepository.findCarroByPlaca(placa);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	@DisplayName("Deve confirmar a existência de um Carro com sucesso a partir de uma placa")
	void deveValidarSeExisteCarroComDeterminadaPlacaCase1() {
		String placa = "ASD-9117";
		carroRepository.save(new Carro(null, "Toyota", 2022, placa, null));
		
		boolean existe = carroRepository.existsByPlaca(placa);
		
		assertTrue(existe);
	}
	
	@Test
	@DisplayName("Não deve confirmar a existência de um Carro com sucesso a partir de uma placa")
	void deveValidarSeExisteCarroComDeterminadaPlacaCase2() {
		String placa = "JDF-9107";
		
		boolean existe = carroRepository.existsByPlaca(placa);
		
		assertFalse(existe);
	}
}
