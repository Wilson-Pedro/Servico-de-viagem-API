package com.wamk.uber.unitarios.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.provider.ViagemEntityAndViagemDtoProviderTets;
import com.wamk.uber.provider.ViagemProviderTest;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.ViagemService;
import com.wamk.uber.services.interfaces.UsuarioService;

@ExtendWith(MockitoExtension.class)
class ViagemServiceTestU {
	
	private final MyObjectMapper modelMapper = mock(MyObjectMapper.class);
	
	private final ViagemRepository viagemRepository = mock(ViagemRepository.class);
	
	private final UsuarioService usuarioService = mock(UsuarioService.class);
	
	private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
	
	private final ViagemService viagemService = new ViagemService(viagemRepository, usuarioService, usuarioRepository, modelMapper);
	
	Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
	
	Motorista motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO);
	
	Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207", motorista);
	
	private final List<Viagem> viagens = List.of(
			new Viagem(1L, 
					"Novo Castelo - Rua das Goiabas 1010", 
					"Pará - Rua das Maçãs", 
					"10 minutos", passageiro, motorista, 
					FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA)
	);
	
	@Test
	void deveSalvarViagemComSucesso_usandoVariavelDeClasse() {
		
		final var viagemEsperada = viagens.get(0);
		
		when(viagemRepository.save(viagemEsperada)).thenReturn(viagemEsperada);
		
		final var viagemSalva = viagemRepository.save(viagemEsperada);
		
		assertThat(viagemSalva).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@ParameterizedTest
	@ArgumentsSource(ViagemEntityAndViagemDtoProviderTets.class)
	void deveSalvarViagemComSucesso_usandoTesteComParametros(ViagemInputDTO viagemInputDTO, 
			Viagem viagemEsperada) {
		
		when(viagemRepository.save(viagemEsperada)).thenReturn(viagemEsperada);
		
		var viagemSalva = viagemRepository.save(viagemEsperada);
		
		assertThat(viagemSalva).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}

	@Test
	void deveBuscarTodasAsViagensComSucesso_usandoVariavelDeClasse() {
		when(viagemRepository.findAll()).thenReturn(viagens);
		
		final var trips = viagemService.findAll();
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(viagens);
	}
	
	@ParameterizedTest
	@ArgumentsSource(ViagemProviderTest.class)
	void deveBuscarTodasAsViagensComSucesso_usandoTesteComParametros(List<Viagem> viagensEsperadas) {
		
		when(viagemRepository.findAll()).thenReturn(viagensEsperadas);
		
		final var trips = viagemService.findAll();
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(viagensEsperadas);
	}
	
	@Test
	void deveBuscarViagemPorIdComSucesso() {
		
		final var viagemEsperada = viagens.get(0);
		
		when(viagemRepository.findById(viagemEsperada.getId())).thenReturn(Optional.of(viagemEsperada));
		
		final var trip = viagemService.findById(viagemEsperada.getId());
		
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}

	@Test
	void deveAtualizarViagemComSucesso() {
		
		final var viagemEsperada = viagens.get(0);
		
		assertEquals("Pará - Rua das Maçãs", viagemEsperada.getDestino());
		
		viagemEsperada.setDestino("Pará - Rua dos limões");
		
		when(viagemRepository.save(viagemEsperada)).thenReturn(viagemEsperada);
		
		final var viagemAtualizada = viagemRepository.save(viagemEsperada);
		
		assertEquals("Pará - Rua dos limões", viagemAtualizada.getDestino());	
	}

	@Test
	void deveDeletarViagemComSucesso() {
		
		final var viagemEsperada = viagens.get(0);
		
		when(viagemRepository.findById(viagemEsperada.getId()))
				.thenReturn(Optional.of(viagemEsperada));
		
		doNothing().when(viagemRepository).delete(viagemEsperada);
		
		viagemRepository.delete(viagemEsperada);
		
		verify(viagemRepository, times(1)).delete(viagemEsperada);
	}
	
	@Test
	void deveMontarUmaViagem_apartirDeUmaSolicitacao() {
		
		final var viagemEsperada = viagens.get(0);
		final var viagem = new Viagem();
		final var solicitacao = new SolicitarViagemDTO(viagemEsperada);
		final var passageiro = viagens.get(0).getPassageiro();
		final var motorista = viagens.get(0).getMotorista();
		
		when(usuarioRepository.findById(passageiro.getId()))
				.thenReturn(Optional.of(passageiro));
		
		when(usuarioRepository.save(passageiro)).thenReturn(passageiro);
		when(usuarioRepository.save(motorista)).thenReturn(motorista);
		
		passageiro.correr();
		motorista.correr();
		
		viagem.setId(1L);
		viagem.setOrigem(solicitacao.getOrigem());
		viagem.setDestino(solicitacao.getDestino());
		viagem.setTempoDeViagem("10 minutos");
		viagem.setPassageiro(passageiro);
		viagem.setMotorista(motorista);
		viagem.setFormaDePagamento(solicitacao.getFormaDePagamento());
		viagem.naoFinalizar();
		
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		
		verify(usuarioRepository, times(1)).save(passageiro);
		verify(usuarioRepository, times(1)).save(motorista);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@Test
	void deveFinalizarViagemComSucesso() {
		
		ViagemStatus finalizada = ViagemStatus.FINALIZADA;
		
		final var viagemEsperado = viagens.get(0);
		
		when(viagemRepository.save(viagemEsperado)).thenReturn(viagemEsperado);
		
		viagemEsperado.finalizar();
		
		viagemRepository.save(viagemEsperado);
		
		assertEquals(finalizada, viagemEsperado.getViagemStatus());
	}
	
	@Test
	void deveBuscarTodasAsViagens_apartirDoPassageiro() {
		
		final var viagensEsperadas = viagens;
		
		when(viagemRepository.findAllByPassageiro(passageiro))
		.thenReturn(viagensEsperadas);
		
		List<Viagem> trips = viagemRepository.findAllByPassageiro(passageiro);
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(viagensEsperadas);
	}
	
	@Test
	void deveBuscarTodasAsViagens_apartitDoMotorista() {
		
		final var viagensEsperadas = viagens;
		
		when(viagemRepository.findAllByMotorista(motorista)).thenReturn(viagens);
		
		List<Viagem> trips = viagemRepository.findAllByMotorista(motorista);
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(viagensEsperadas);
	}
	
	@Test
	void deveBuscarViagemPorPassageiro() {
		final var viagemEsperada = viagens.get(0);
		
		when(viagemRepository.findByPassageiro(passageiro)).thenReturn(viagemEsperada);
		
		final var viagem = viagemService.acharViagemPorPassageiro(passageiro);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@Test
	void deveBuscarViagemPorMotorista() {
		final var viagemEsperada = viagens.get(0);
		
		when(viagemRepository.findByMotorista(motorista)).thenReturn(viagemEsperada);
		
		final var viagem = viagemService.acharViagemPorMotorista(motorista);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@Test
	void devePaginarUmaListaDeCarrosComSucesso() {
		
		List<Viagem> list = this.viagens;
		Page<Viagem> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());
		
		when(viagemService.findAll(any(Pageable.class))).thenReturn(page);
		
		Page<Viagem> pageViagem = viagemService.findAll(PageRequest.of(0, 10));
		
		assertNotNull(pageViagem);
		assertEquals(pageViagem.getContent().size(), list.size());
	}
}
