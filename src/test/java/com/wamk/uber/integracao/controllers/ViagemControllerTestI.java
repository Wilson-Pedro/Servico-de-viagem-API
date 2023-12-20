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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.enums.FormaDePagamento;
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
	
	private static String VIAGEM_ENDPOINT = "/viagens";

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

	@Test
	@Order(1)
	void deveRegistraUsuarioParaLoginComSucesso() {
		RegistroDTO registroDTO = new RegistroDTO("pedro", "34567", UserRole.ADMIN);
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(registroDTO.getPassword());
		
		assertNotNull(encryptedPassword);
		assertNotEquals(encryptedPassword, registroDTO.getPassword());
		
		User newUser = new User(registroDTO.getLogin(), encryptedPassword, registroDTO.getRole());
		
		//assertEquals(0, userRepository.count());
		
		userRepository.save(newUser);
		
//		assertEquals(1, userRepository.count());
//		assertEquals(UserRole.ADMIN, registroDTO.getRole());
		
	}
	
	@Test
	@Order(2)
	void deveRealizarLoginComSucesso() {
		AuthenticationDTO dto = new AuthenticationDTO("pedro", "34567");
		var usernamePassowrd = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
		var auth = this.authenticationManager.authenticate(usernamePassowrd);
		var token = this.tokenService.generateToken((User) auth.getPrincipal());
		
		assertNotNull(token);
		TOKEN = token;
	}
	
	@Test
	@Order(3)
	void deveBuscarTodasAsViagensComSucesso() throws Exception {
		
		mockMvc.perform(get(VIAGEM_ENDPOINT)
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
				.andExpect(jsonPath("$.id", equalTo(id.intValue())))
				.andExpect(jsonPath("$.origem", equalTo("Novo Castelo - Rua das Goiabas 1010")))
				.andExpect(jsonPath("$.destino", equalTo("Pará - Rua das Maçãs")))
				.andExpect(jsonPath("$.tempoDeViagem", equalTo("10 minutos")))
				.andExpect(jsonPath("$.nomePassageiro", equalTo("Wilson")))
				.andExpect(jsonPath("$.nomeMotorista", equalTo("Pedro")))
				.andExpect(jsonPath("$.formaDePagamento", equalTo("PIX")))
				.andExpect(jsonPath("$.viagemStatus", equalTo("NAO_FINALIZADA")))
				.andReturn();
	}
	
	@Test
	@Order(5)
	void deveRegistrarUmaViagemComSucesso() throws Exception {
		
		ViagemInputDTO inputDto = new ViagemInputDTO( 
						"Novo Castelo - Rua das Goiabas 1010", 
						"Pará - Rua das Maçãs", 
						"10 minutos", 2L, 5L, FormaDePagamento.PAYPAL.getDescricao());
		
		String jsonRequest = objectMapper.writeValueAsString(inputDto);
		
		assertEquals(1, viagemRepository.count());
		
		mockMvc.perform(post(VIAGEM_ENDPOINT + "/")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", equalTo(2)))
				.andExpect(jsonPath("$.origem", equalTo("Novo Castelo - Rua das Goiabas 1010")))
				.andExpect(jsonPath("$.destino", equalTo("Pará - Rua das Maçãs")))
				.andExpect(jsonPath("$.tempoDeViagem", equalTo("10 minutos")))
				.andExpect(jsonPath("$.nomePassageiro", equalTo("Ana")))
				.andExpect(jsonPath("$.nomeMotorista", equalTo("Julia")))
				.andExpect(jsonPath("$.formaDePagamento", equalTo("PAYPAL")))
				.andExpect(jsonPath("$.viagemStatus", equalTo("NAO_FINALIZADA")))
				.andReturn();
		
		assertEquals(2, viagemRepository.count());
		
	}
	
	@Test
	@Order(6)
	void deveAtualizarViagemComSucesso() throws Exception {
		ViagemInputDTO inputDto = new ViagemInputDTO( 
				"Novo Castelo - Rua das Goiabas 1010", 
				"Pará - Rua das Melancias", 
				"20 minutos", 2L, 5L, FormaDePagamento.PAYPAL.getDescricao());
		
		Viagem viagem = viagemService.findById(2L);
		
		assertNotEquals(viagem.getDestino(), inputDto.getDestino());
		
		String josnRequest = objectMapper.writeValueAsString(inputDto);
		
		mockMvc.perform(put(VIAGEM_ENDPOINT + "/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(josnRequest))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.destino", equalTo("Pará - Rua das Melancias")))
				.andReturn();
		
		Viagem viagemAtualizada = viagemService.findById(2L);
		
		assertEquals(viagemAtualizada.getDestino(), inputDto.getDestino());
	}
	
	@Test
	@Order(7)
	void deveDeletarViagemComSucesso() throws Exception {
		
		assertEquals(2, viagemRepository.count());
		
		mockMvc.perform(delete(VIAGEM_ENDPOINT + "/{id}", 2L)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(1, viagemRepository.count());
		
	}
	
	@Test
	@Order(8)
	void deveCancelarViagemComSucesso() throws Exception {
		
		assertEquals(1, viagemRepository.count());
		
		mockMvc.perform(delete(VIAGEM_ENDPOINT + "/{id}/cancelar", 1L)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(0, viagemRepository.count());
		
	}

	@Test
	@Order(9)
	void deveFinalizarViagemComSucesso() throws Exception {
		
		Passageiro p = (Passageiro) usuarioRepository.findById(3L).get();
		
		Motorista m = (Motorista) usuarioRepository.findById(5L).get();
		
		Viagem viagem = new Viagem(null, 
				"Novo Castelo - Rua das Goiabas 1010", "Pará - Rua das Maçãs", 
				"10 minutos", p, m, FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
		

		viagemRepository.save(viagem);
		
		Long id = viagem.getId();
		
		assertEquals(ViagemStatus.NAO_FINALIZADA, viagem.getViagemStatus());
		
		mockMvc.perform(patch(VIAGEM_ENDPOINT + "/{id}/finalizar", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		Viagem viagemEsperada = viagemService.findById(id);
		
		assertEquals(ViagemStatus.FINALIZADA, viagemEsperada.getViagemStatus());
	}
	
	@Test
	@Order(10)
	void devePaginarUmaListaDeViagensComSucesso() throws Exception {
		
		var viagens = viagemService.findAll();
		Page<Viagem> page = new PageImpl<>(viagens, PageRequest.of(0, 10), viagens.size());
		
		mockMvc.perform(get(VIAGEM_ENDPOINT + "/pages")
				.header("Authorization", "Bearer " + TOKEN))
		 		.andExpect(status().isOk())
		 		.andReturn();
		
		assertEquals(page.getContent().size(), viagens.size());
		
	}
}