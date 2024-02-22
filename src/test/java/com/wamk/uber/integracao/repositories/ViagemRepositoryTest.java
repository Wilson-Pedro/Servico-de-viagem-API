package com.wamk.uber.integracao.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ViagemRepositoryTest {

	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
	
	Motorista motorista = new Motorista(2L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO);
	
	
	Viagem viagem = new Viagem(1L, 
					"Novo Castelo - Rua das Goiabas 1010", 
					"Pará - Rua das Maçãs", 
					"10 minutos", passageiro, motorista, 
					FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
	
	@BeforeEach
	void setUp() {
		usuarioRepository.deleteAll();
		viagemRepository.deleteAll();
	}
	
	@Test
	@DisplayName("Deve buscar viagem a partir do passageiro com sucesso.")
	@Order(1)
	void deveBuscarViagemAPartirDoPassageiroComSucesso() {
		
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		viagemRepository.save(viagem);
		
		Viagem viagemEsperada = viagemRepository.findById(1L).get();
		Viagem trip = viagemRepository.findByPassageiro(passageiro);
		
		assertNotNull(trip);
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@Test
	@DisplayName("Deve buscar viagem a partir do motorista com sucesso.")
	void deveBuscarViagemAPartirDoMotoristaComSucesso() {
		
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		viagemRepository.save(viagem);
		
		Long id = viagemRepository.findAll().get(0).getId();
		
		Viagem viagemEsperada = viagemRepository.findById(id).get();
		Viagem trip = viagemRepository.findByMotorista(motorista);
		
		assertNotNull(trip);
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@Test
	@DisplayName("Deve buscar todas as viagem a partir do passageiro com sucesso.")
	void deveBuscarTodasAsViagemAPartirDoPassageiroComSucesso() {
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		viagemRepository.save(viagem);
		
		List<Viagem> viagensEsperadas = viagemRepository.findAll();
		List<Viagem> viagens = viagemRepository.findAllByPassageiro(passageiro);
		
		assertNotNull(viagens);
		assertFalse(viagens.isEmpty());
		assertThat(viagens).usingRecursiveComparison().isEqualTo(viagensEsperadas);
	}
	
	@Test
	@DisplayName("Deve buscar todas as viagem a partir do motorista com sucesso.")
	void deveBuscarTodasAsViagemAPartirDoMotoristaComSucesso() {
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		viagemRepository.save(viagem);

		List<Viagem> viagensEsperadas = viagemRepository.findAll();
		List<Viagem> viagens = viagemRepository.findAllByMotorista(motorista);
		
		assertNotNull(viagens);
		assertFalse(viagens.isEmpty());
		assertThat(viagens).usingRecursiveComparison().isEqualTo(viagensEsperadas);
	}
	
	@Test
	@DisplayName("Não deve confirmar a existência de uma Viagem com sucesso a partir de seu Id")
	void deveConfirmarAExistenciaDeUmaViagemAPartirDoIdCase1() {
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		viagemRepository.save(viagem);
		
		Long id = viagemRepository.findAll().get(0).getId();
		
		boolean existe = viagemRepository.existsById(id);
		
		assertTrue(existe);
	}
	
	@Test
	@DisplayName("Não deve confirmar a existência de uma Viagem com sucesso a partir de seu Id")
	void deveConfirmarAExistenciaDeUmaViagemAPartirDoIdCase2() {
		Long id = 2L;
		
		boolean existe = viagemRepository.existsById(id);
		
		assertFalse(existe);
	}
}
