package com.wamk.uber.integracao.controllers;

import static com.wamk.uber.LoginUniversal.TOKEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.roles.UserRole;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UserRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.UsuarioService;
import com.wamk.uber.services.ViagemService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioControllerTestI {
	
	private static String USUARIO_ENDPOINT = "/usuarios";

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	TokenService tokenService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	ViagemService viagemService;
	
	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	@Test
	@Order(1)
	void deveRegistraUsuarioParaLoginComSucesso() {
		RegistroDTO registroDTO = new RegistroDTO("lara", "56789", UserRole.ADMIN);
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(registroDTO.getPassword());
		
		assertNotNull(encryptedPassword);
		assertNotEquals(encryptedPassword, registroDTO.getPassword());
		
		User newUser = new User(registroDTO.getLogin(), encryptedPassword, registroDTO.getRole());
		
		userRepository.save(newUser);
		
		assertEquals(UserRole.ADMIN, registroDTO.getRole());
		
	}
	
	@Test
	@Order(2)
	void deveRealizarLoginComSucesso() {
		AuthenticationDTO dto = new AuthenticationDTO("lara", "56789");
		var usernamePassowrd = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
		var auth = this.authenticationManager.authenticate(usernamePassowrd);
		var token = this.tokenService.generateToken((User) auth.getPrincipal());
		
		assertNotNull(token);
		TOKEN = token;
	}
	
	@Test
	@Order(3)
	void deveBuscarTodosOsUsuariosComSucesso() throws Exception {
		
		mockMvc.perform(get(USUARIO_ENDPOINT)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();	
		
		assertEquals(6, usuarioRepository.count());
	}
	
	@Test
	@Order(4)
	void deveBuscarUsuarioAPartirDoIdComSucesso() throws Exception {
		
		Long id = usuarioService.findById(1L).getId();
		
		mockMvc.perform(get(USUARIO_ENDPOINT + "/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(id.intValue())))
				.andExpect(jsonPath("$.nome", equalTo("Wilson")))
				.andExpect(jsonPath("$.telefone", equalTo("9816923456")))
				.andExpect(jsonPath("$.tipoUsuario", equalTo("Passageiro")))
				.andReturn();	
	}
	
	@Test
	@Order(5)
	void deveBuscarViagensAPartirDoUserIdComSucesso() throws Exception {
		
		Long id = usuarioService.findById(1L).getId();
		
		mockMvc.perform(get("/usuarios/{id}/viagens", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();	
	}
	
	@Test
	@Order(6)
	void deveRegistarUsuarioComSucesso() throws Exception {
		
		UsuarioDTO usuarioDto = new UsuarioDTO(null, "Lucia", "(66)98273-9281", "Passageiro");
		
		String jsonRequest = objectMapper.writeValueAsString(usuarioDto);
		
		mockMvc.perform(post(USUARIO_ENDPOINT + "/")
				.header("Authorization", "Bearer " + TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", equalTo(7)))
				.andExpect(jsonPath("$.nome", equalTo("Lucia")))
				.andExpect(jsonPath("$.telefone", equalTo("(66)98273-9281")))
				.andExpect(jsonPath("$.tipoUsuario", equalTo("Passageiro")))
				.andReturn();
		
		assertEquals(7, usuarioRepository.count());
	}
	
	@Test
	@Order(7)
	void deveAtualizarUsuarioComSucesso() throws Exception {
		
		Usuario usuario = usuarioService.findById(2L);
		
		Long id = usuario.getId();
		
		UsuarioDTO usuarioDto = new UsuarioDTO(null, "Ana", "(66)98321-5237", "Passageiro");
		
		assertNotEquals(usuario.getTelefone(), usuarioDto.getTelefone());
		
		String jsonRequest = objectMapper.writeValueAsString(usuarioDto);
		
		mockMvc.perform(put(USUARIO_ENDPOINT + "/{id}", id)
				.header("Authorization", "Bearer " + TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.telefone", equalTo("(66)98321-5237")))
				.andReturn();
		
		Usuario usuarioAtualizado = usuarioService.findById(2L);
		
		assertEquals(usuarioAtualizado.getTelefone(), usuarioDto.getTelefone());
	}
	
	@Test
	@Order(8)
	void deveDeletarUsuarioAPartirDoIdComSucesso() throws Exception {
		
		Long id = usuarioService.findById(2L).getId();
		
		assertEquals(7, usuarioRepository.count());
		
		mockMvc.perform(delete(USUARIO_ENDPOINT + "/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(6, usuarioRepository.count());
	}
	
	@Test
	@Order(9)
	void deveSolicitarViagemComSucesso() throws Exception {
		
		Passageiro passageiro = new Passageiro
				(null, "Silva", "(69)98647-6866", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		
		usuarioRepository.save(passageiro);
		
		SolicitarViagemDTO solcitagem = new SolicitarViagemDTO
				(passageiro.getId(), "Rua da Luz", "Rua das Goiabas", "250", FormaDePagamento.PAYPAL);
		
		String jsonRequest = objectMapper.writeValueAsString(solcitagem);
		
		assertEquals(1, viagemRepository.count());
		
		mockMvc.perform(post(USUARIO_ENDPOINT + "/solicitacarViagem")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		assertEquals(2, viagemRepository.count());
	}
	
	@Test
	@Order(10)
	void deveDeveCancelarViagemAPartirDoUserIdComSucesso() throws Exception {
		
		Passageiro passageiro = new Passageiro
				(null, "Junho", "(69)98937-6526", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		
		usuarioRepository.save(passageiro);
		
		SolicitarViagemDTO solcitagem = new SolicitarViagemDTO
				(passageiro.getId(), "Rua da Luz", "Rua das Goiabas", "250", FormaDePagamento.PAYPAL);
		
		viagemService.solicitandoViagem(solcitagem);
		
		assertEquals(3, viagemRepository.count());
		
		mockMvc.perform(delete(USUARIO_ENDPOINT + "/{usuarioId}/cancelarViagem", passageiro.getId())
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(2, viagemRepository.count());
	}
	
	@Test
	@Order(11)
	void deveDesativarUsuarioComSucesso() throws Exception {
		
		Passageiro passageiro = new Passageiro
				(8L, "Gil", "(69)98321-6812", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		
		usuarioRepository.save(passageiro);
		
		Long id = passageiro.getId();
		
		UsuarioStatus desativo = UsuarioStatus.DESATIVADO;
		
		assertNotEquals(desativo, passageiro.getUsuarioStatus());
		
		mockMvc.perform(patch(USUARIO_ENDPOINT + "/{id}/desativar", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
		
		
		Passageiro passageiroDesativado = (Passageiro) usuarioService.findById(8L);
		
		assertEquals(desativo, passageiroDesativado.getUsuarioStatus());
	}
	
	@Test
	@Order(12)
	void deveAtivarUsuarioComSucesso() throws Exception {
		
		Passageiro passageiro = new Passageiro
				(9L, "Laura", "(69)98321-6812", TipoUsuario.PASSAGEIRO, UsuarioStatus.DESATIVADO);
		
		usuarioRepository.save(passageiro);
		
		Long id = passageiro.getId();
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		assertNotEquals(ativo, passageiro.getUsuarioStatus());
		
		mockMvc.perform(patch(USUARIO_ENDPOINT + "/{id}/ativar", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
		
		Passageiro passageiroAtivado = (Passageiro) usuarioService.findById(id);
		
		assertEquals(ativo, passageiroAtivado.getUsuarioStatus());
	}
}