package com.wamk.uber.integracao.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.UsuarioService;

@SpringBootTest
class UsuarioServiceTestI {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	CarroRepository carroRepository;
	
	List<Carro> carros = List.of(
			new Carro(1L, "Fiat", 2022, "JVF-9207"),
			new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
			new Carro(3L, "Forger", 2022, "FTG-0160")
	);
	
	List<Usuario> usuarios = List.of(
			new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, carros.get(0)),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO, carros.get(1)),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO, carros.get(2))
	);
	
	Viagem viagem = new Viagem(1L, "Novo Castelo - Rua das Goiabas 1010", 
			"Pará - Rua das Maçãs", "20min", 
			(Passageiro)usuarios.get(0), (Motorista) usuarios.get(3), 
			FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
	
	@BeforeEach
	void setup() {
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
		viagemRepository.deleteAll();
	}

	@Test
	void deveSalvarUsuarioComSucesso() {
		Passageiro passageiro = new Passageiro(1L, "Julio", "98123411456", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		UsuarioDTO usuarioDto = new UsuarioDTO(passageiro);
		
		usuarioService.save(usuarioDto);
		
		assertEquals(1, usuarioRepository.count());
	}
	
	@Transactional
	@Test
	void deveRetoranTodosOsCarrosComSucesso() {
		carroRepository.saveAll(carros);
		usuarioRepository.saveAll(usuarios);
		List<Usuario> users = usuarioService.findAll();
		
		assertNotEquals(null, users);
		assertThat(users).usingRecursiveComparison().isEqualTo(usuarios);
		assertEquals(6, usuarioRepository.count());
		
	}
	
	@Transactional
	@Test
	void deveBuscarUsarioAPartirDoIdComSucesso() {
		usuarioRepository.save(usuarios.get(0));
		Long id = usuarios.get(0).getId();
		var usuario = usuarioService.findById(id);
		
		assertNotEquals(null, usuario);
		assertThat(usuario).usingRecursiveComparison().isEqualTo(usuarios.get(0));
	}
	
	@Transactional
	@Test
	void deveBuscarMotoristaPorStatus() {
		carroRepository.saveAll(carros);
		usuarioRepository.saveAll(usuarios);
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		Motorista motorista = usuarioService.buscarMotoristaPorStatus(ativo);
		
		assertNotEquals(null, motorista);
		assertThat(motorista).usingRecursiveComparison().isEqualTo(usuarios.get(4));
	}
	
	@Transactional
	@Test
	void deveAtualizarUsuarioComSucesso() {
		usuarioRepository.save(usuarios.get(0));
		Long id = usuarios.get(0).getId();
		var usuario = usuarioService.findById(id);
		
		assertNotEquals("Pedro", usuario.getNome());
		
		usuario.setNome("Pedro");
		var usuarioDto = new UsuarioDTO(usuario);
		var usuarioAtualizado = usuarioService.atualizarCadastro(usuarioDto, id);
		
		assertEquals("Pedro", usuarioAtualizado.getNome());
	}
	
	@Transactional
	@Test
	void deveDeletarUsuarioComSucesso() {
		var usuario = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		usuarioRepository.save(usuario);
		Long id = usuario.getId();
		
		assertNotEquals(null, usuario);
		assertEquals(1, usuarioRepository.count());
		
		usuarioService.delete(id);
		
		assertEquals(0, usuarioRepository.count());
	}
	
//	@Test
//	void deveAticarUsuariosAParitrDaViagemId() {
//		carroRepository.saveAll(carros);
//		usuarioRepository.saveAll(usuarios);
//		viagemRepository.save(viagem);
//		
//		Passageiro passageiro = (Passageiro) usuarioService.findById(viagem.getPassageiro().getId());
//		Motorista motorista = (Motorista) usuarioService.findById(viagem.getMotorista().getId());
//		usuarioService.ativarUsuarioPorViagemId(viagem.getId());
//		
//		UsuarioStatus ativo = UsuarioStatus.ATIVO;
//		
//		assertEquals(ativo, viagem.getMotorista().getUsuarioStatus());
//		assertEquals(ativo, viagem.getPassageiro().getUsuarioStatus());
//	}
}	
