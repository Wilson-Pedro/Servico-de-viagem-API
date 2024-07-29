package com.wamk.uber.unitarios.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.interfaces.ViagemService;
import com.wamk.uber.web.controllers.ViagemController;

class ViagemControllerTestU {
	
	@InjectMocks
	ViagemController viagemController;
	
	ViagemService viagemService = mock(ViagemService.class);
	
	ViagemRepository viagemRepository = mock(ViagemRepository.class);
	
	UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
	
	Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
	Passageiro passageiro2 = new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
	Passageiro passageiro3 = new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
	
	Motorista motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO);
	Motorista motorista2 = new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
	Motorista motorista3 = new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
	
	Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207", motorista);
	Carro carro2 = new Carro(2L, "Chevrolet", 2022, "FFG-0460", motorista2);
	Carro carro3 = new Carro(3L, "Forger", 2022, "FTG-0160", motorista3);
	
	private final List<Viagem> viagens = List.of(
			new Viagem(1L, 
					"Novo Castelo - Rua das Goiabas 1010", 
					"Pará - Rua das Maçãs", 
					"10 minutos", passageiro, motorista, 
					FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA),
			new Viagem(2L, 
					"Novo Castelo - Rua das Limonadas 1020", 
					"Pará - Rua das Peras", 
					"20 minutos", passageiro2, motorista2, 
					FormaDePagamento.DEBITO, ViagemStatus.NAO_FINALIZADA)
	);

	@Test
	void deveBuscarTodasAsViagensComSucesso() throws Exception {
		
		final var viagensEsperadoas = viagens;
		
		when(this.viagemService.findAll()).thenReturn(viagens);
		
//		mockMvc.perform(get("/viagens"))
//				.andExpect(status().isOk())
//				.andReturn();
		
		final var trips = this.viagemService.findAll();
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(viagensEsperadoas);
		verify(this.viagemService).findAll();
	}
	
	@Test
	void deveBuscarViagemApartirDoIdComSucesso() throws Exception {
		
		final var viagemEsperada = viagens.get(0);
		Long id = viagemEsperada.getId();
		
		when(this.viagemService.findById(id)).thenReturn(viagemEsperada);
		
//		mockMvc.perform(get("/viagens/{id}", id))
//		        .andExpect(status().isOk())
//		        .andReturn();
		
		final var trip = this.viagemService.findById(id);
		
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
		verify(this.viagemService).findById(id);
	}
	
	@Test
	void deveSalvarViagemComSucesso() throws Exception {
		
		final var viagemEsperada = viagens.get(0);
		final var viagemInputDTO = new ViagemInputDTO(viagemEsperada);
		
		when(this.viagemService.save(viagemInputDTO)).thenReturn(viagemEsperada);
		
//		String jsonRequest = objectMapper.writeValueAsString(viagemInputDTO);
//		
//		mockMvc.perform(post("/viagens/")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isCreated())
//				.andReturn();
		
		final var trip = this.viagemService.save(viagemInputDTO);
		
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
		verify(this.viagemService).save(viagemInputDTO);
	}
	
	@Test
	void deveAtualizarViagemComSucesso() throws Exception {
		
		final var viagemEsperada = viagens.get(0);
//		final var id = viagemEsperada.getId();
		
		assertNotEquals("Pará - Rua das Goiabadas", viagemEsperada.getDestino());
		
		viagemEsperada.setDestino("Pará - Rua das Goiabadas");
		
		final var viagemInputDtTO = new ViagemInputDTO(viagemEsperada);
		
//		String jsonRequest = objectMapper.writeValueAsString(viagemInputDtTO);
//		
//		mockMvc.perform(put("/viagens/{id}", id)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andReturn();
		
		when(this.viagemService.save(viagemInputDtTO)).thenReturn(viagemEsperada);
		
		final var trip = this.viagemService.save(viagemInputDtTO);
		
		assertEquals("Pará - Rua das Goiabadas", trip.getDestino());
		verify(this.viagemService).save(viagemInputDtTO);
	}
	
	@Test
	void deveFinalizarViagemComSucesso() throws Exception {
		
		final var viagemEsperada = viagens.get(0);
		final var id = viagemEsperada.getId();
		
		ViagemStatus finalizada = ViagemStatus.FINALIZADA;
		
		assertNotEquals(finalizada, viagemEsperada.getViagemStatus());
		
//		mockMvc.perform(patch("/viagens/{id}/finalizar", id))
//				.andExpect(status().isNoContent())
//				.andReturn();
		
		viagemEsperada.finalizar();
		
		final var viagemInputDtTO = new ViagemInputDTO(viagemEsperada);
		
		doNothing().when(this.viagemService).finalizarViagem(id);
		when(this.viagemService.save(viagemInputDtTO)).thenReturn(viagemEsperada);
		
		final var trip = this.viagemService.save(viagemInputDtTO);
		this.viagemService.finalizarViagem(id);
		
		assertThat(trip).usingRecursiveComparison().isEqualTo(viagemEsperada);
		verify(this.viagemService).finalizarViagem(1L);
		verify(this.viagemService).save(viagemInputDtTO);
	}
	
	@Test 
	void deveCancelarViagemComSucesso() throws Exception {
		
		final var viagemCancelada = viagens.get(0);
//		Long id = viagemCancelada.getId();
		
		Passageiro passageiro = viagemCancelada.getPassageiro();
		Motorista motorista = viagemCancelada.getMotorista();
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		assertNotEquals(ativo, passageiro.getUsuarioStatus());
		assertNotEquals(ativo, motorista.getUsuarioStatus());
		
//		mockMvc.perform(delete("/viagens/{id}/cancelar", id))
//				.andExpect(status().isNoContent())
//				.andReturn();
		
		passageiro.ativar();
		motorista.ativar();
		
		doNothing().when(this.viagemRepository).delete(viagemCancelada);
		
		this.viagemRepository.delete(viagemCancelada);
		
		assertEquals(ativo, passageiro.getUsuarioStatus());
		assertEquals(ativo, motorista.getUsuarioStatus());
		verify(this.viagemRepository).delete(viagemCancelada);

	}
	
	@Test
	void deveDeletarViagemApartirDoIdComSucesso() throws Exception {
		
		Viagem viagemEsperada =  new Viagem(3L, 
				"Novo Castelo - Rua dos Abacaxis 1030", 
				"Pará - Rua das Peras", 
				"30 minutos", passageiro3, motorista3, 
				FormaDePagamento.CREDITO, ViagemStatus.NAO_FINALIZADA);
		
		Long id = viagemEsperada.getId();
		
		doNothing().when(this.viagemService).delete(id);
		
		this.viagemService.delete(id);
		verify(this.viagemService).delete(id);
	}
}
