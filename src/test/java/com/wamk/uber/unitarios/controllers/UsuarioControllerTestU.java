package com.wamk.uber.unitarios.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.controllers.UsuarioController;
import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.services.UsuarioService;
import com.wamk.uber.services.ViagemService;

public class UsuarioControllerTestU {

	@InjectMocks
	UsuarioController usuarioController;
	
	UsuarioService usuarioService = mock(UsuarioService.class);
	
	ViagemService viagemService = mock(ViagemService.class);
	
	UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
	
	private final List<Usuario> usuarios = List.of(
			new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO)
	);
	
	//private final List<UsuarioDTO> usuariosDTO = usuarios.stream().map(x -> new UsuarioDTO(x)).toList();
	
	private final List<Viagem> viagens = List.of(
			new Viagem(1L, "Novo Castelo - Rua das Goiabas 1010", 
					"Pará - Rua das Maçãs", "20 minutos", 
					(Passageiro)usuarios.get(0), (Motorista) usuarios.get(3), 
					FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA)
	);
	
	
	@Test
	void deveBuscarTodosOsUsuariosComSucesso() throws Exception {
		
		final var usuariosEsperados = usuarios;
		
		when(this.usuarioService.findAll()).thenReturn(usuariosEsperados);
		
//		mockMvc.perform(get("/usuarios"))
//				.andExpect(status().isOk())
//				.andReturn();
		
		final var users = this.usuarioService.findAll();
		
		assertThat(users).usingRecursiveComparison().isEqualTo(usuariosEsperados);
		verify(this.usuarioService).findAll();
	}
	
	@Test
	void deveBuscarUsuariApartirDoIdComSucesso() throws Exception {
		
		final var usuarioEsperado = usuarios.get(0);
		Long id = usuarioEsperado.getId();
		
		when(this.usuarioService.findById(id)).thenReturn(usuarioEsperado);
		
//		mockMvc.perform(get("/usuarios/{id}", id))
//				.andExpect(status().isOk())
//				.andReturn();
		
		final var usuario = this.usuarioService.findById(id);
		
		assertThat(usuario).usingRecursiveComparison().isEqualTo(usuarioEsperado);
		verify(usuarioService).findById(id);
	}
	
	@Test
	@DisplayName("Deve retornar todas as viagens relacionadas ao Usuario a partir do UserId")
	void deveBuscarTodasAsviagensApartirDoUserId() throws Exception {
		
		final var viagensEsperadas = viagens;
		Long id = usuarios.get(0).getId();
		
		when(this.viagemService.buscarTodasAsViagensPorUserId(id)).thenReturn(viagensEsperadas);
		
//		mockMvc.perform(get("/usuarios/{id}/viagens", id))
//				.andExpect(status().isOk())
//				.andReturn();
		
		final var trips = this.viagemService.buscarTodasAsViagensPorUserId(id);
		
		assertThat(trips).usingRecursiveComparison().isEqualTo(viagensEsperadas);
		verify(this.viagemService).buscarTodasAsViagensPorUserId(id);
	}
	
	@Test
	void deveSalvarUsuarioComSucesso() throws Exception {
		
		final var usuarioEsperado = new Passageiro
				(7L, "Gilberto", "9812813902", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		final var usuarioDTO = new UsuarioDTO(usuarioEsperado);
		
		when(this.usuarioService.save(usuarioDTO)).thenReturn(usuarioEsperado);
		
//		String jsonRequest = objectMapper.writeValueAsString(usuarioDTO);
//		
//		mockMvc.perform(post("/usuarios/")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isCreated())
//				.andReturn();
		
		final var usuario = this.usuarioService.save(usuarioDTO);
		
		assertThat(usuario).usingRecursiveComparison().isEqualTo(usuarioEsperado);
		verify(this.usuarioService).save(usuarioDTO);
	}
	
	@Test
	void deveAtualizarUsuarioComSucesso() throws Exception {
		
		final var usuarioEsperado = usuarios.get(1);
//		final var id = usuarioEsperado.getId();
		
		assertNotEquals("Paula", usuarioEsperado.getNome());
		
		usuarioEsperado.setNome("Paula");
		
		final var userDto = new UsuarioDTO(usuarioEsperado);
		
//		String jsonRequest = objectMapper.writeValueAsString(userDto);
//		
//		mockMvc.perform(put("/usuarios/{id}", id)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andReturn();
		
		when(this.usuarioService.save(userDto)).thenReturn(usuarioEsperado);
		
		final var userUpdated = this.usuarioService.save(userDto);
		
		assertEquals("Paula", userUpdated.getNome());
		verify(usuarioService).save(userDto);
	}
	
	@Test
	void deveDeletarUsuarioApartirDoIdComSucesso() throws Exception {
		
		final var usuarioEsperado = usuarios.get(2);
		final var id = usuarioEsperado.getId();
		
		doNothing().when(this.usuarioService).delete(id);
		
//		mockMvc.perform(delete("/usuarios/{id}", id))
//				.andExpect(status().isNoContent())
//				.andReturn();
		
		this.usuarioService.delete(id);
		verify(this.usuarioService).delete(id);
	}
	
	@Test
	void deveSolicitarViagemComSucesso() throws Exception {
		
		final var viagemEsperada = viagens.get(0);
		final var viagem = new Viagem();
		final var solicitacao = new SolicitarViagemDTO(viagemEsperada);
		
//		String jsonRequest = objectMapper.writeValueAsString(solicitacao);
//		
//		mockMvc.perform(post("/usuarios/solicitacarViagem")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//		 		.andExpect(status().isNoContent())
//		 		.andReturn();
		
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
		viagem.setTempoDeViagem("20 minutos");
		viagem.setPassageiro(passageiro);
		viagem.setMotorista(motorista);
		viagem.setFormaDePagamento(solicitacao.getFormaDePagamento());
		viagem.naoFinalizar();
		
		usuarioRepository.save(passageiro);
		usuarioRepository.save(motorista);
		
		verify(usuarioRepository).save(passageiro);
		verify(usuarioRepository).save(motorista);
		
		assertThat(viagem).usingRecursiveComparison().isEqualTo(viagemEsperada);
	}
}
