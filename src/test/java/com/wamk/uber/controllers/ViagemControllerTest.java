package com.wamk.uber.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
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
}
