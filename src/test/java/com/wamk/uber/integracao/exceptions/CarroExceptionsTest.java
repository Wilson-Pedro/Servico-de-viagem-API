package com.wamk.uber.integracao.exceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.input.CarroMinDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.services.CarroServiceImpl;
import com.wamk.uber.services.interfaces.CarroService;

@SpringBootTest
class CarroExceptionsTest {
	
	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	CarroService carroService;
	
	@Autowired
	CarroServiceImpl carroServiceImpl;
	
	List<Motorista> motoristas = List.of(
			new Motorista(1L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO),
			new Motorista(2L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO),
			new Motorista(3L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO)
	);
	
	List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207",motoristas.get(0)),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460",motoristas.get(1)),
			new Carro(3L, "Forger", 2022, "FTG-0160",motoristas.get(2))
	);
	
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
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
		
		usuarioRepository.save(motoristas.get(0));
		carroRepository.save(new Carro(3L, "Forger", 2022, "FTG-0160", motoristas.get(0)));
		
		Motorista motorista = new Motorista(null, "Lara", "9833683829", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
		usuarioRepository.save(motorista);
		
		final var carroDto = new CarroMinDTO(12L, "Fiat", 2023, "FTG-0160", motorista.getId());
		
		assertThrows(PlacaExistenteException.class, () -> this.carroService.save(carroDto));
	}
	
	@Test
	@DisplayName("Deve lançar a Exceção: PlacaExistenteException")
	void deveLancarExcecaoTentarAtualizarUmCarro() {
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
		
		usuarioRepository.save(motoristas.get(0));
		carroRepository.save(new Carro(3L, "Forger", 2022, "FTG-0160", motoristas.get(0)));
		
		Motorista motorista = new Motorista(null, "Lara", "9833683829", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
		usuarioRepository.save(motorista);
		
		Long motoristaId = usuarioRepository.findAll().get(1).getId();
		
		final var carroDto = new CarroMinDTO(12L, "Fiat", 2023, "FTG-0160", motoristaId);
		Long id = carroRepository.findAll().get(0).getId();
		
		assertThrows(PlacaExistenteException.class, 
				() -> this.carroService.atualizarCadastro(carroServiceImpl.toCarroDto(carroDto), id));
	}
}
