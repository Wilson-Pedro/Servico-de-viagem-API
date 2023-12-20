package com.wamk.uber.integracao.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.CarroService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarroServiceTestI {

	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	CarroService carroService;
	
	List<Usuario> usuarios = List.of(
			new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO)
	);
	
	List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207",(Motorista) usuarios.get(3)),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460",(Motorista) usuarios.get(4)),
			new Carro(3L, "Forger", 2022, "FTG-0160",(Motorista) usuarios.get(5))
	);
	
	
	@BeforeEach
	void setup() {
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
	}
	
	
	@Test
	@Order(1)
	void deveSalvarCarroComSucesso() {
		CarroDTO carroDto = new CarroDTO(null, "Fiat", 2022, "JVF-9207");
		
		assertEquals(0, carroRepository.count());
		
		carroService.save(carroDto);
		
		Long id = carroRepository.findAll().get(0).getId();
		
		var carro = carroService.findById(id);

		assertNotNull(carro);
		assertNotNull(carro.getId());
		assertTrue(carro.getId() > 0);
		assertEquals(1, carroRepository.count());
	}
	
	@Test
	@Order(2)
	void deveBuscarTodosOsCarrosComSucesso() {
		usuarioRepository.saveAll(usuarios);
		carroRepository.saveAll(carros);
		
		List<Carro> carros = carroService.findAll();
		
		assertNotNull(carros);
		assertEquals(carros.size(), carroRepository.count());
	}

	@Test
	@Order(3)
	void deveBuscarCarroAPartirDoIdComSucesso() {
		Carro carroEsperado = new Carro(null, "Fiat", 2022, "JVF-9207");
		carroRepository.save(carroEsperado);
		
		Carro carro = carroService.findById(carroEsperado.getId());
		
		assertNotNull(carro);
		assertEquals("Fiat", carro.getModelo());
		assertEquals(2022, carro.getAno());
		assertEquals("JVF-9207", carro.getPlaca());
	}
	
	@Test
	@Order(4)
	void deveDeletarCarroComSucesso() {
		Carro carroEsperado = new Carro(null, "Fiat", 2022, "JGF-9107");
		carroRepository.save(carroEsperado);
		
		assertEquals(1, carroRepository.count());
		
		carroService.delete(carroEsperado.getId());
		
		assertEquals(0, carroRepository.count());
	}
	
	@Test
	@Order(5)
	void deveAtualizarCarroComSucesso() {
		Carro carro = new Carro(null, "Fiat", 2022, "JVF-9207");
		carroRepository.save(carro);
		
		assertNotEquals("Toyota", carro.getModelo());
		
		carro.setModelo("Toyota");
		
		CarroDTO carroDto = new CarroDTO(carro);
		carro = carroService.atualizarCadastro(carroDto, carroDto.getId());
		
		assertEquals("Toyota", carro.getModelo());
	}
}
