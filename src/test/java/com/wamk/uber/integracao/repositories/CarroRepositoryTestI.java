package com.wamk.uber.integracao.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.repositories.CarroRepository;

@DataJpaTest
@ActiveProfiles("test")
class CarroRepositoryTestI {
	
	@Autowired
	CarroRepository carroRepository;

	@Test
	@DisplayName("Deve Buscar Carro a partir de uma plca que existe com sucesso")
	void deveBuscarCarroComSucessoCase1() {
		String placa = "JHG-9117";
		CarroDTO carroDTO = new CarroDTO(1L, "Fiat", 2022, placa);
		this.createCarro(carroDTO);
		
		Optional<Carro> result = this.carroRepository.findCarroByPlaca(placa);
		
		assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Não deve Buscar Carro com sucesso a partir de uma placa que não existe")
	void deveBuscarCarroComSucessoCase2() {
		String placa = "JHG-9117";
		
		Optional<Carro> result = this.carroRepository.findCarroByPlaca(placa);
		
		assertThat(result.isEmpty()).isTrue();
	}
	
	@Test
	@DisplayName("Deve confirmar a existência de um Carro com sucesso a partir de uma placa")
	void deveValidarSeExisteCarroComDeterminadaPlacaCase1() {
		String placa = "JVF-9207";
		
		boolean existe = carroRepository.existsByPlaca(placa);
		
		assertEquals(true, existe);
	}
	
	@Test
	@DisplayName("Não deve confirmar a existência de um Carro com sucesso a partir de uma placa")
	void deveValidarSeExisteCarroComDeterminadaPlacaCase2() {
		String placa = "JDF-9107";
		
		boolean existe = carroRepository.existsByPlaca(placa);
		
		assertEquals(false, existe);
	}
	
	private Carro createCarro(CarroDTO carroDTO) {
		Carro newCar = new Carro(carroDTO);
		return carroRepository.save(newCar);
	}
}
