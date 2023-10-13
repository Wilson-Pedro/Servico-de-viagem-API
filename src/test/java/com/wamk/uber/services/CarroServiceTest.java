package com.wamk.uber.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
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
	
	Carro car1;
	Carro car2;
	Carro car3;
	
	List<Carro> carros = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		car1 = new Carro(1L, "Fiat", 2022, "JVF-9207");
		car2 = new Carro(2L, "Chevrolet", 2022, "FFG-0460");
		car3 = new Carro(3L, "Forger", 2022, "FTG-0160");
		
		carros.add(car1);
		carros.add(car2);
		carros.add(car3);
	}
	
	@Test
	void deveSalvarCarroComSucesso() {
		CarroDTO carroDTO = new CarroDTO(car1);
		when(carroRepository.save(car1)).thenReturn(car1);
		Carro carroSalvo = carroService.save(carroDTO);
		assertEquals(car1, carroSalvo);
	}
	
	@Test
	void deveBuscarTodosOsCarrosComSucesso() {
		when(carroRepository.findAll()).thenReturn(carros);
		List<Carro> cars = carroService.findAll();
		assertEquals(carros, cars);
	}
	
	@Test
	void deveBuscarCarroPorIdComSucesso() {
		when(carroRepository.findById(car1.getId()))
		.thenReturn(Optional.of(car1));
		Carro car = carroService.findById(car1.getId());
		assertEquals(car1, car);
	}
	
	@Test
	void deveAtualizarCarroComSucesso() {
		when(carroRepository.save(car1)).thenReturn(car1);
		car1.setModelo("Toyota");
		CarroDTO carroDTO = new CarroDTO(car1);
		Carro carroAtualizado = carroService.save(carroDTO);
		assertEquals("Toyota", carroAtualizado.getModelo());
	}
	
	@Test
	void deveDeletarCarroComSucesso() {
		carroRepository.delete(carros.get(0));
		verify(carroRepository).delete(carros.get(0));
	}
}