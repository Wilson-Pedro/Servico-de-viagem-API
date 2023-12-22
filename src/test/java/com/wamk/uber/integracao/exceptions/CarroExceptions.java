package com.wamk.uber.integracao.exceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.input.CarroInputDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.services.CarroService;

@SpringBootTest
class CarroExceptions {
	
	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	CarroService carroService;
	
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
	
	@BeforeEach
	void setUp() {
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
	}
	
	@Test
	@DisplayName("Deve lançar exceção: EntidadeNaoEncontradaException")
	void deveLancarExcecaoAposTentarBuscarUmCarroInexistente() {
		
		Long id = 40L;
		
		assertThrows(EntidadeNaoEncontradaException.class, 
				() -> this.carroService.findById(id));
	}

	@Test
	@DisplayName("Deve lançar a Exceção: PlacaExistenteException")
	void deveLancarExcecaoAposTentarRegistrarUmCarro() {
		
		usuarioRepository.saveAll(motoristas);
		carroRepository.saveAll(carros);
		
		Motorista motorista = new Motorista(null, "Lara", "9833683829", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
		usuarioRepository.save(motorista);
		
		final var carroDto = new CarroInputDTO(12L, "Fiat", 2023, "JVF-9207", motorista.getId());
		
		assertThrows(PlacaExistenteException.class, () -> this.carroService.save(carroDto));
	}
	
	@Test
	@DisplayName("Deve lançar a Exceção: PlacaExistenteException")
	void deveLancarExcecaoTentarAtualizarUmCarro() {
		
		usuarioRepository.saveAll(motoristas);
		carroRepository.saveAll(carros);
		
		Motorista motorista = new Motorista(null, "Lara", "9833683829", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
		usuarioRepository.save(motorista);
		
		final var carroDto = new CarroInputDTO(12L, "Fiat", 2023, "JVF-9207", motorista.getId());
		Long id = carroRepository.findAll().get(0).getId();
		
		assertThrows(PlacaExistenteException.class, 
				() -> this.carroService.atualizarCadastro(carroService.toCarroDto(carroDto), id));
	}
}
