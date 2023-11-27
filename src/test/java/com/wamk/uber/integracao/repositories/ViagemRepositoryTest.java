package com.wamk.uber.integracao.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@DataJpaTest
@ActiveProfiles("test")
class ViagemRepositoryTest {

	@Autowired
	ViagemRepository viagemRepository;
	
	@Test
	@DisplayName("Deve buscar viagem a partir do passageiro com sucesso.")
	void deveBuscarViagemAPartirDoPassageiroComSucesso() {
		Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		
		Viagem viagemEsperada = viagemRepository.findById(1L).get();
		Viagem trip = viagemRepository.findByPassageiro(passageiro);
		
		assertNotNull(trip);
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@Test
	@DisplayName("Deve buscar viagem a partir do motorista com sucesso.")
	void deveBuscarViagemAPartirDoMotoristaComSucesso() {
		Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
		Motorista motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, carro);
		
		Viagem viagemEsperada = viagemRepository.findById(1L).get();
		Viagem trip = viagemRepository.findByMotorista(motorista);
		
		assertNotNull(trip);
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@Test
	@DisplayName("Deve buscar todas as viagem a partir do passageiro com sucesso.")
	void deveBuscarTodasAsViagemAPartirDoPassageiroComSucesso() {
		Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		
		List<Viagem> viagensEsperadas = viagemRepository.findAll();
		List<Viagem> viagens = viagemRepository.findAllByPassageiro(passageiro);
		
		assertNotNull(viagens);
		assertFalse(viagens.isEmpty());
		assertThat(viagens).usingRecursiveComparison().isEqualTo(viagensEsperadas);
	}
	
	@Test
	@DisplayName("Deve buscar todas as viagem a partir do motorista com sucesso.")
	void deveBuscarTodasAsViagemAPartirDoMotoristaComSucesso() {
		Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
		Motorista motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, carro);
		
		List<Viagem> viagensEsperadas = viagemRepository.findAll();
		List<Viagem> viagens = viagemRepository.findAllByMotorista(motorista);
		
		assertNotNull(viagens);
		assertFalse(viagens.isEmpty());
		assertThat(viagens).usingRecursiveComparison().isEqualTo(viagensEsperadas);
	}
	
	@Test
	@DisplayName("Não deve confirmar a existência de uma Viagem com sucesso a partir de seu Id")
	void deveConfirmarAExistenciaDeUmaViagemAPartirDoIdCase1() {
		Long id = 1L;
		
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
