package com.wamk.uber.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.services.UsuarioService;
import com.wamk.uber.services.ViagemService;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

	@InjectMocks
	UsuarioController usuarioController;
	
	UsuarioService usuarioService = mock(UsuarioService.class);
	
	ViagemService viagemService = mock(ViagemService.class);
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private final List<Usuario> usuarios = List.of(
			new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, null),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO, null),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO, null)
	);
	
	//private final List<UsuarioDTO> usuariosDTO = usuarios.stream().map(x -> new UsuarioDTO(x)).toList();
	
	private final List<Viagem> viagens = List.of(
			new Viagem(1L, "Novo Castelo - Rua das Goiabas 1010", 
					"Pará - Rua das Maçãs", "20min", 
					(Passageiro)usuarios.get(0), (Motorista) usuarios.get(3), 
					FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA)
	);
	
	@Test
	void deveBuscarTodosOsUsuariosComSucesso() throws Exception {
		
		final var usersExpected = usuarios;
		
		when(this.usuarioService.findAll()).thenReturn(usersExpected);
		
		mockMvc.perform(get("/usuarios"))
				.andExpect(status().isOk())
				.andReturn();
		
		final var users = this.usuarioService.findAll();
		
		assertThat(users).usingRecursiveComparison().isEqualTo(usersExpected);
		verify(this.usuarioService, times(1)).findAll();
	}
	
	@Test
	void deveBuscarUsuariApartirDoIdComSucesso() throws Exception {
		
		final var userExpected = usuarios.get(0);
		Long id = userExpected.getId();
		
		when(this.usuarioService.findById(id)).thenReturn(userExpected);
		
		mockMvc.perform(get("/usuarios/{id}", id))
				.andExpect(status().isOk())
				.andReturn();
		
		final var usuario = this.usuarioService.findById(id);
		
		assertThat(usuario).usingRecursiveComparison().isEqualTo(userExpected);
		verify(usuarioService, times(1)).findById(id);
	}
	
	@Test
	@DisplayName("Deve retornar todas as viagens relacionadas ao Usuario a partir do UserId")
	void deveBuscarTodasAsviagensApartirDoUserId() throws Exception {
		
		final var tripsExpected = viagens;
		Long id = usuarios.get(0).getId();
		
		when(this.viagemService.getAllTripsByUserId(id)).thenReturn(tripsExpected);
		
		mockMvc.perform(get("/usuarios/{id}/viagens", id))
				.andExpect(status().isOk())
				.andReturn();
		
		final var trips = this.viagemService.getAllTripsByUserId(id);
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(tripsExpected);
		verify(this.viagemService, times(1)).getAllTripsByUserId(id);
	}
	
	@Test
	void deveSalvarUsuarioComSucesso() throws Exception {
		
		final var userExpected = new Passageiro
				(7L, "Gilberto", "9812813902", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		final var userDTO = new UsuarioDTO(userExpected);
		
		when(this.usuarioService.save(userDTO)).thenReturn(userExpected);
		
		String jsonRequest = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(post("/usuarios/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andReturn();
		
		final var usuario = this.usuarioService.save(userDTO);
		
		assertThat(usuario).usingRecursiveComparison().isEqualTo(userExpected);
		verify(this.usuarioService, times(1)).save(userDTO);
	}
	
	@Test
	void deveAtualizarUsuarioComSucesso() throws Exception {
		
		final var userExpected = usuarios.get(1);
		final var userDTO = new UsuarioDTO(userExpected);
		
		Long id = userExpected.getId();
		
		when(this.usuarioService.save(userDTO)).thenReturn(userExpected);
		
		assertNotEquals("Paula", userExpected.getNome());
		
		userExpected.setNome("Paula");
		
		final var userDtoUpdated = new UsuarioDTO(userExpected);
		
		String jsonRequest = objectMapper.writeValueAsString(userDtoUpdated);
		
		mockMvc.perform(put("/usuarios/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		when(this.usuarioService.save(userDtoUpdated)).thenReturn(userExpected);
		
		final var userUpdated = this.usuarioService.save(userDtoUpdated);
		
		assertEquals("Paula", userUpdated.getNome());
		verify(usuarioService, times(1)).save(userDtoUpdated);
	}
}
