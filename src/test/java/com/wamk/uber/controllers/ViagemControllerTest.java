package com.wamk.uber.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.services.UsuarioService;
import com.wamk.uber.services.ViagemService;

@SpringBootTest
@AutoConfigureMockMvc
class ViagemControllerTest {
	
	@InjectMocks
	ViagemController viagemController;
	
	ViagemService viagemService = mock(ViagemService.class);
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
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
	void deveBuscarTodasAsViagensComSucesso() throws Exception {
		
		final var tripsExpected = viagens;
		
		when(this.viagemService.findAll()).thenReturn(viagens);
		
		mockMvc.perform(get("/viagens"))
				.andExpect(status().isOk())
				.andReturn();
		
		final var trips = this.viagemService.findAll();
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(tripsExpected);
		verify(this.viagemService, timeout(1)).findAll();
	}
	
	@Test
	void deveBuscarViagemApartirDoIdComSucesso() throws Exception {
		
		final var tripExpected = viagens.get(0);
		Long id = tripExpected.getId();
		
		when(this.viagemService.findById(id)).thenReturn(tripExpected);
		
		mockMvc.perform(get("/viagens/{id}", id))
		        .andExpect(status().isOk())
		        .andReturn();
		
		final var trip = this.viagemService.findById(id);
		
		assertThat(trip).usingRecursiveComparison().isEqualTo(tripExpected);
		verify(this.viagemService, times(1)).findById(id);
	}
	
	@Test
	void deveSalvarViagemComSucesso() throws Exception {
		
		final var tripExpected = viagens.get(0);
		final var viagemInputDTO = new ViagemInputDTO(tripExpected);
		
		when(this.viagemService.save(viagemInputDTO)).thenReturn(tripExpected);
		
		String jsonRequest = objectMapper.writeValueAsString(viagemInputDTO);
		
		mockMvc.perform(post("/viagens/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andReturn();
		
		final var trip = this.viagemService.save(viagemInputDTO);
		
		assertThat(trip).usingRecursiveComparison().isEqualTo(tripExpected);
		verify(this.viagemService, times(1)).save(viagemInputDTO);
	}
	
	@Test
	void deveAtualizarViagemComSucesso() throws Exception {
		
		final var tripExpected = viagens.get(0);
		final var id = tripExpected.getId();
		
		assertNotEquals("Pará - Rua das Goiabadas", tripExpected.getDestino());
		
		tripExpected.setDestino("Pará - Rua das Goiabadas");
		
		final var viagemInputDtTO = new ViagemInputDTO(tripExpected);
		
		String jsonRequest = objectMapper.writeValueAsString(viagemInputDtTO);
		
		mockMvc.perform(put("/viagens/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		when(this.viagemService.save(viagemInputDtTO)).thenReturn(tripExpected);
		
		final var trip = this.viagemService.save(viagemInputDtTO);
		
		assertEquals("Pará - Rua das Goiabadas", trip.getDestino());
		verify(this.viagemService, timeout(1)).save(viagemInputDtTO);
	}
	
	@Test
	void deveFinalizarViagemComSucesso() throws Exception {
		
		final var tripExpected = viagens.get(0);
		final var id = tripExpected.getId();
		
		ViagemStatus finalizada = ViagemStatus.FINALIZADA;
		
		assertNotEquals(finalizada, tripExpected.getViagemStatus());
		
		mockMvc.perform(patch("/viagens/{id}/finalizar", id))
				.andExpect(status().isNoContent())
				.andReturn();
		
		tripExpected.finalizar();
		
		final var viagemInputDtTO = new ViagemInputDTO(tripExpected);
		
		doNothing().when(this.viagemService).finishTrip(id);
		when(this.viagemService.save(viagemInputDtTO)).thenReturn(tripExpected);
		
		final var trip = this.viagemService.save(viagemInputDtTO);
		this.viagemService.finishTrip(id);
		
		assertThat(trip).usingRecursiveComparison().isEqualTo(tripExpected);
		verify(this.viagemService, times(1)).finishTrip(1L);
		verify(this.viagemService, times(1)).save(viagemInputDtTO);
	}
}