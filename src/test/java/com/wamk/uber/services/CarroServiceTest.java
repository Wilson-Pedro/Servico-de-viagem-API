package com.wamk.uber.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.repositories.CarroRepository;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {
	
	@InjectMocks
	CarroService carroService;
	
	@Mock
	CarroRepository carroRepository;
	
	List<Carro> carros = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		carros.add(new Carro(1L, "Fiat", 2022, "JVF-9207"));
		carros.add(new Carro(2L, "Chevrolet", 2022, "FFG-0460"));
		carros.add(new Carro(3L, "Forger", 2022, "FTG-0160"));
	}
	
	@Test
	void deveSalvarCarroComSucesso() {
		CarroDTO carroDTO = new CarroDTO(carros.get(0));
		when(carroRepository.save(carros.get(0))).thenReturn(carros.get(0));
		Carro carroSalvo = carroService.save(carroDTO);
		assertEquals(carros.get(0), carroSalvo);
	}
	
	@Test
	void deveBuscarTodosOsCarrosComSucesso() {
		when(carroRepository.findAll()).thenReturn(carros);
		List<Carro> cars = carroService.findAll();
		assertEquals(carros, cars);
	}
	
	@Test
	void deveBuscarCarroPorIdComSucesso() {
		when(carroRepository.findById(carros.get(0).getId()))
		.thenReturn(Optional.of(carros.get(0)));
		Carro car = carroService.findById(carros.get(0).getId());
		assertEquals(carros.get(0), car);
	}
}
