package com.wamk.uber.integracao.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioServiceTestI {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	CarroRepository carroRepository;
	
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
	@Order(1)
	void deveSalvarUsuarioComSucesso() {
		Passageiro passageiro = new Passageiro(1L, "Julio", "98123411456", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		UsuarioDTO usuarioDto = new UsuarioDTO(passageiro);
		
		usuarioService.save(usuarioDto);
		
		assertEquals(1, usuarioRepository.count());
	}
	
	@Transactional
	@Test
	@Order(2)
	void deveRetornarTodosOsUsuarioComSucesso() {
		usuarioRepository.saveAll(usuarios);
		carroRepository.saveAll(carros);
		List<Usuario> users = usuarioService.findAll();
		
		assertNotNull(users);
		assertThat(users).usingRecursiveComparison().isEqualTo(usuarios);
		assertEquals(users.size(), usuarioRepository.count());
		
	}
	
	@Transactional
	@Test
	@Order(3)
	void deveBuscarUsarioAPartirDoIdComSucesso() {
		usuarioRepository.save(usuarios.get(0));
		Long id = usuarios.get(0).getId();
		var usuario = usuarioService.findById(id);
		
		assertNotEquals(null, usuario);
		assertThat(usuario).usingRecursiveComparison().isEqualTo(usuarios.get(0));
	}
	
	@Transactional
	@Test
	@Order(4)
	void deveBuscarMotoristaPorStatus() {
		usuarioRepository.saveAll(usuarios);
		carroRepository.saveAll(carros);
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		Motorista motorista = usuarioService.buscarMotoristaPorStatus(ativo);
		
		assertNotEquals(null, motorista);
		assertThat(motorista).usingRecursiveComparison().isEqualTo(usuarios.get(4));
	}
	
	@Transactional
	@Test
	@Order(5)
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
	@Order(6)
	void deveDeletarUsuarioComSucesso() {
		var usuario = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		usuarioRepository.save(usuario);
		Long id = usuario.getId();
		
		assertNotNull(usuario);
		assertEquals(1, usuarioRepository.count());
		
		usuarioService.delete(id);
		
		assertEquals(0, usuarioRepository.count());
	}
	
	@Transactional
	@Test
	@Order(7)
	void deveAtivarUsuariosAParitrDaViagemId() {
		usuarioRepository.saveAll(usuarios);
		carroRepository.saveAll(carros);
		viagemRepository.save(viagem);
		
		Passageiro passageiro = (Passageiro) usuarioService.findById(viagem.getPassageiro().getId());
		Motorista motorista = (Motorista) usuarioService.findById(viagem.getMotorista().getId());
		usuarioService.ativarUsuarioPorViagemId(viagem.getId());
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		assertEquals(ativo, passageiro.getUsuarioStatus());
		assertEquals(ativo, motorista.getUsuarioStatus());
	}
	
	@Transactional
	@Test
	@Order(8)
	void deveDesativarUsuarioComSucesso() {
		usuarioRepository.saveAll(usuarios);
		carroRepository.saveAll(carros);
		viagemRepository.save(viagem);
		
		usuarioService.desativarUsuario(usuarios.get(2).getId());
		
		UsuarioStatus desativo = UsuarioStatus.DESATIVADO;
		
		Usuario usuario = usuarioService.findById(usuarios.get(2).getId());
		
		assertEquals(desativo, usuario.getUsuarioStatus());
	}
	
//	@Test
//	@Order(9)
//	void deveSalvarTudoComSucesso() {
//		usuarioRepository.saveAll(usuarios);
//		carroRepository.saveAll(carros);
//		viagemRepository.save(viagem);
//	}
	
}
