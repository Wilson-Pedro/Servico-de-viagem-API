package com.wamk.uber.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.wamk.uber.provider.ViagemEntityAndViagemDtoProviderTets;
import com.wamk.uber.provider.ViagemProviderTest;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@ExtendWith(MockitoExtension.class)
class ViagemServiceTest {
	
	private final ViagemRepository viagemRepository = mock(ViagemRepository.class);
	
	private final UsuarioService usuarioService = mock(UsuarioService.class);
	
	private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
	
	private final ViagemService viagemService = new ViagemService(viagemRepository, usuarioService, usuarioRepository);
	
	Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
	
	Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
	
	Motorista motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, carro);
	
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
		final var viagemInputDTO = new ViagemInputDTO(viagemEsperada);
		
		when(viagemRepository.save(viagemEsperada)).thenReturn(viagemEsperada);
		
		final var viagemSalva = viagemService.save(viagemInputDTO);
		
		assertThat(viagemSalva).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
	
	@ParameterizedTest
	@ArgumentsSource(ViagemEntityAndViagemDtoProviderTets.class)
	void deveSalvarViagemComSucesso_usandoTesteComParametros(ViagemInputDTO viagemInputDTO, 
			Viagem viagemEsperada) {
		
		when(viagemRepository.save(viagemEsperada)).thenReturn(viagemEsperada);
		
		var viagemSalva = viagemService.save(viagemInputDTO);
		
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
		
		when(viagemRepository.save(viagemEsperada)).thenReturn(viagemEsperada);
		
		assertEquals("Pará - Rua das Maçãs", viagemEsperada.getDestino());
		
		viagemEsperada.setDestino("Pará - Rua dos limões");
		
		ViagemInputDTO viagemInputDTO = new ViagemInputDTO(viagemEsperada);
		final var viagemAtualizada = viagemService.save(viagemInputDTO);
		
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
	void deveMontarUmaViagem_ApartirDeUmaSolicitacao() {
		
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
		viagem.naoFinalizada();
		
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		
		verify(usuarioRepository, times(1)).save(passageiro);
		verify(usuarioRepository, times(1)).save(motorista);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
}
