package com.wamk.uber.integracao.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
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
import com.wamk.uber.services.interfaces.ViagemService;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ViagemServiceTestI {
	
	@Autowired
	ViagemService viagemService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	CarroRepository carroRepository;
	
	private Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
	
	private Motorista motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO);
	
	Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207", motorista);
	
	private List<Viagem> viagens = List.of(
			new Viagem(1L, 
					"Novo Castelo - Rua das Goiabas 1010", 
					"Pará - Rua das Maçãs", 
					"10 minutos", passageiro, motorista, 
					FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA)
	);
	
	@BeforeEach
	void setup() {
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
		viagemRepository.deleteAll();
	}
	
	@Test
	void deveSalvarViagemComSucesso() {
		var passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		var motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO);
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		var viagemEsperada = new Viagem(1L, 
				"Novo Castelo - Rua das Goiabas 1010", 
				"Pará - Rua das Maçãs", 
				"10 minutos", passageiro, motorista, 
				FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
		
		var dto = new ViagemInputDTO(viagemEsperada);
		
		assertEquals(0, viagemRepository.count());
		
		var viagem = viagemService.save(dto);
		
		assertNotEquals(null, viagem);
		assertEquals(1, viagemRepository.count());
	}
	
	@Transactional
	@Test
	void deveRetornarTodasAsViagensComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		List<Viagem> viagensEsperadas = viagemService.findAll();
		
		assertNotNull(viagensEsperadas);
		assertEquals(viagensEsperadas.size(), viagemRepository.count());
		assertThat(viagensEsperadas).usingRecursiveComparison().isEqualTo(viagens);
	}
	
	@Transactional
	@Test
	void deveBuscarViagemApartirDoIdComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		var viagem = viagemService.findById(viagens.get(0).getId());
		Long id = viagemRepository.findAll().get(0).getId();
		
		assertNotNull(viagem);
		assertNotNull(viagem);
		assertEquals(id, viagem.getId());
		assertEquals("Novo Castelo - Rua das Goiabas 1010", viagem.getOrigem());
		assertEquals("Pará - Rua das Maçãs", viagem.getDestino());
		assertEquals("10 minutos", viagem.getTempoDeViagem());
		assertEquals(FormaDePagamento.PIX, viagem.getFormaDePagamento());
		assertEquals(ViagemStatus.NAO_FINALIZADA, viagem.getViagemStatus());
		assertThat(passageiro).usingRecursiveComparison().isEqualTo(viagem.getPassageiro());
		assertThat(motorista).usingRecursiveComparison().isEqualTo(viagem.getMotorista());
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagens.get(0));
	}
	
	@Transactional
	@Test
	void deveAtualizarViagemComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		Long id = viagens.get(0).getId();
		var viagem = viagemService.findById(id);
		
		assertNotEquals("Pará - Ruas das Goiabadas", viagem.getDestino());
		viagem.setDestino("Pará - Ruas das Goiabadas");
		
		var dto = new ViagemInputDTO(viagem);
		var viagemAtualizada = viagemService.atualizarCadastro(dto, id);
		
		assertEquals("Pará - Ruas das Goiabadas", viagemAtualizada.getDestino());
	}
	
	@Transactional
	@Test
	void deveDeletarViagemComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		Long id = viagens.get(0).getId();
		
		assertEquals(1, viagemRepository.count());
		
		viagemService.delete(id);
		
		assertEquals(0, viagemRepository.count());
	}
	
	@Transactional
	@Test
	void deveCancelarViagemComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		Long id = viagens.get(0).getId();
		var viagem = viagemService.findById(viagens.get(0).getId());
		viagem.setViagemStatus(ViagemStatus.NAO_FINALIZADA);
		viagemRepository.save(viagem);
		
		viagemService.cancelarViagemPorViagemId(id);
		
		assertEquals(0, viagemRepository.count());
	}
	
	@Transactional
	@Test
	void deveCancelarViagemPorUserIdComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		Long id = passageiro.getId();
		var viagem = viagemService.findById(viagens.get(0).getId());
		viagem.setViagemStatus(ViagemStatus.NAO_FINALIZADA);
		viagemRepository.save(viagem);
		
		viagemService.cancelarViagemPorUserId(id);
		
		assertEquals(0, viagemRepository.count());
	}
	
	@Transactional
	@Test
	void deveAcharViagemPorUserIdComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		Long id = passageiro.getId();
		
		var viagem = viagemService.acharViagemPorUserId(id);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagens.get(0));
		
	}
	
	@Transactional
	@Test
	void deveBuscarTodasAsViagemPorUserIdComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		Long id = passageiro.getId();
		
		List<Viagem> trips = viagemService.buscarTodasAsViagensPorUserId(id);
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(viagens);
		
	}
	
	@Transactional
	@Test
	void deveAcharViagemPorPassageiro() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		var viagem = viagemService.acharViagemPorPassageiro(passageiro);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagens.get(0));
		
	}
	
	@Transactional
	@Test
	void deveAcharViagemPorMotorista() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		var viagem = viagemService.acharViagemPorMotorista(motorista);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagens.get(0));
		
	}
	
	@Transactional
	@Test
	void deveConstruirViagemComSucesso() {
		var passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		var motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		
		assertEquals(0, viagemRepository.count());
		
		var solicitacao = new SolicitarViagemDTO(passageiro.getId(), 
				"Novo Castelo - Rua das Goiabas 1010", "Pará - Rua das Maçãs", 
				"5630 metros", FormaDePagamento.PIX);
		
		viagemService.solicitandoViagem(solicitacao);
		
		assertEquals(1, viagemRepository.count());
	}
	
	@Transactional
	@Test
	void deveFinalizarViagemComSucesso() {
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		carroRepository.save(carro);
		viagemRepository.saveAll(viagens);
		
		Long id = viagens.get(0).getId();
		
		viagemService.finalizarViagem(id);
		var viagem = viagemService.findById(id);
		assertEquals(ViagemStatus.FINALIZADA, viagem.getViagemStatus());
	}
} 