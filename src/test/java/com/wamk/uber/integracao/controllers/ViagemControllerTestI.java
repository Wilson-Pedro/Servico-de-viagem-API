package com.wamk.uber.integracao.controllers;

import static com.wamk.uber.LoginUniversal.LOGIN;
import static com.wamk.uber.LoginUniversal.PASSWORD;
import static com.wamk.uber.LoginUniversal.TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.enums.roles.UserRole;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UserRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.ViagemService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ViagemControllerTestI {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	TokenService tokenService;
	
	@Autowired
	ViagemService viagemService;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
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
	@Order(1)
	void deveRegistraUsuarioComSucesso() {
		RegistroDTO registroDTO = new RegistroDTO(LOGIN, PASSWORD, UserRole.ADMIN);
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(registroDTO.getPassword());
		
		assertNotNull(encryptedPassword);
		assertNotEquals(encryptedPassword, registroDTO.getPassword());
		
		User newUser = new User(registroDTO.getLogin(), encryptedPassword, registroDTO.getRole());
		
		assertEquals(0, userRepository.count());
		
		userRepository.save(newUser);
		
		assertEquals(1, userRepository.count());
		assertEquals(UserRole.ADMIN, registroDTO.getRole());
		
	}
	
	@Test
	@Order(2)
	void deveRealizarLoginComSucesso() {
		AuthenticationDTO dto = new AuthenticationDTO(LOGIN, PASSWORD);
		var usernamePassowrd = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
		var auth = this.authenticationManager.authenticate(usernamePassowrd);
		var token = this.tokenService.generateToken((User) auth.getPrincipal());
		
		assertNotNull(token);
		TOKEN = token;
	}
	
	@Test
	@Order(3)
	void deveBuscarTodasAsViagensComSucesso() throws Exception {
		
		mockMvc.perform(get("/viagens")
				.header("Authorization", "Bearer " + TOKEN))
		.andExpect(status().isOk())
		.andReturn();
	}
	
	@Test
	@Order(4)
	void deveBuscarViagemPorIdComSucesso() throws Exception {
		
		Long id = viagemService.findById(1L).getId();
		
		mockMvc.perform(get("/viagens/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(5)
	void deveRegistrarUmaViagemComSucesso() throws Exception {
		
		ViagemInputDTO inputDto = new ViagemInputDTO(null, 
						"Novo Castelo - Rua das Goiabas 1010", 
						"Pará - Rua das Maçãs", 
						"10 minutos", 2L, 5L, FormaDePagamento.PAYPAL.getDescricao());
		
		String jsonRequest = objectMapper.writeValueAsString(inputDto);
		
		assertEquals(1, viagemRepository.count());
		
		mockMvc.perform(post("/viagens/")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andReturn();
		
		assertEquals(2, viagemRepository.count());
		
	}
	
	@Test
	@Order(6)
	void deveAtualizarViagemComSucesso() throws Exception {
		ViagemInputDTO inputDto = new ViagemInputDTO(null, 
				"Novo Castelo - Rua das Goiabas 1010", 
				"Pará - Rua das Melancias", 
				"20 minutos", 2L, 5L, FormaDePagamento.PAYPAL.getDescricao());
		
		String josnRequest = objectMapper.writeValueAsString(inputDto);
		
		mockMvc.perform(put("/viagens/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(josnRequest))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(7)
	void deveDeletarViagemComSucesso() throws Exception {
		
		assertEquals(2, viagemRepository.count());
		
		mockMvc.perform(delete("/viagens/{id}", 2L)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(1, viagemRepository.count());
		
	}
	
	@Test
	@Order(8)
	void deveCancelarViagemComSucesso() throws Exception {
		
		assertEquals(1, viagemRepository.count());
		
		mockMvc.perform(delete("/viagens/{id}/cancelar", 1L)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(0, viagemRepository.count());
		
	}

	@Test
	@Order(9)
	void deveFinalizarViagemComSucesso() throws Exception {
		ViagemInputDTO inputDto = new ViagemInputDTO(null, 
				"Novo Castelo - Rua das Goiabas 1010", 
				"Pará - Rua das Maçãs", 
				"10 minutos", 3L, 6L, FormaDePagamento.PAYPAL.getDescricao());
		
		viagemService.save(inputDto);
		
		assertEquals(1, viagemRepository.count());
		
		Viagem viagem = viagemService.findById(3L);
		
		assertEquals(ViagemStatus.NAO_FINALIZADA, viagem.getViagemStatus());
		
		mockMvc.perform(patch("/viagens/{id}/finalizar", 3L)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		Viagem viagemEsperada = viagemService.findById(3L);
		
		assertEquals(ViagemStatus.FINALIZADA, viagemEsperada.getViagemStatus());
	}
} 
