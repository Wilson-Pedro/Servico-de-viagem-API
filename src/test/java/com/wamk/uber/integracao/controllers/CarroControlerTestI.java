package com.wamk.uber.integracao.controllers;

import static com.wamk.uber.LoginUniversal.TOKEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.input.CarroMinDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.roles.UserRole;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UserRepository;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.services.interfaces.CarroService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarroControlerTestI {
	
	private static String  CAR_ENDPOINT = "/carros";

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	TokenService tokenService;
	
	@Autowired
	CarroService carroService;
	
	@Autowired
	CarroRepository carroRepository;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	@Order(1)
	void deleteAll() {
		carroRepository.deleteAll();
		usuarioRepository.deleteAll();
	}

	@Test
	@Order(2)
	void deveRegistraUsuarioParaLoginComSucesso() {
		userRepository.deleteAll();
		
		RegistroDTO registroDTO = new RegistroDTO("wilson", "12345", UserRole.ADMIN);
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(registroDTO.getPassword());
		
		assertNotNull(encryptedPassword);
		assertNotEquals(encryptedPassword, registroDTO.getPassword());
		
		User newUser = new User(registroDTO.getLogin(), encryptedPassword, registroDTO.getRole());
		
		userRepository.save(newUser);
		
		assertEquals(UserRole.ADMIN, registroDTO.getRole());
	}
	
	@Test
	@Order(3)
	void deveRealizarLoginComSucesso() {
		AuthenticationDTO dto = new AuthenticationDTO("wilson", "12345");
		var usernamePassowrd = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
		var auth = this.authenticationManager.authenticate(usernamePassowrd);
		var token = this.tokenService.generateToken((User) auth.getPrincipal());
		
		assertNotNull(token);
		TOKEN = token;
	}
	
	@Test
	@Order(4)
	void deveRegistrarCarroComSucesso() throws Exception {
		var motorista = new Motorista(1L, "Carla", "9896928-1345", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO);
		usuarioRepository.save(motorista);
		
		CarroMinDTO carroDto = new CarroMinDTO(null, "Toyota", 2022, "HRS-0305", motorista.getId());
		
		String jsonRequest = objectMapper.writeValueAsString(carroDto);
		
		assertEquals(0, carroRepository.count());
		
		mockMvc.perform(post(CAR_ENDPOINT + "/")
				.header("Authorization", "Bearer " + TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.modelo", equalTo("Toyota")))
				.andExpect(jsonPath("$.ano", equalTo(2022)))
				.andExpect(jsonPath("$.placa", equalTo("HRS-0305")))
				.andReturn();
		
		assertEquals(1, carroRepository.count());
	}
	
	@Test
	@Order(5)
	void deveBuscarTodosOsCarrosComSucesso() throws Exception {
		
		mockMvc.perform(get(CAR_ENDPOINT)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(6)
	void deveBuscarCarroAPartirDoIdComSucesso() throws Exception {
		
		Long id = carroService.findAll().get(0).getId();
		
		mockMvc.perform(get(CAR_ENDPOINT + "/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(id.intValue())))
				.andExpect(jsonPath("$.modelo", equalTo("Toyota")))
				.andExpect(jsonPath("$.ano", equalTo(2022)))
				.andExpect(jsonPath("$.placa", equalTo("HRS-0305")))
				.andReturn();
	}
	
	@Test
	@Order(7)
	void deveAtualizarCarroComSucesso() throws Exception {
		
		Long id = carroService.findAll().get(0).getId();
		
		Carro carro = carroService.findById(id);
		
		CarroDTO carroDto = new CarroDTO(null, "Sedans", 2023, "JVD-4401");
		
		assertNotEquals(carro.getModelo(), carroDto.getModelo());
		
		String jsonRequest = objectMapper.writeValueAsString(carroDto);
		
		mockMvc.perform(put(CAR_ENDPOINT + "/{id}", id)
				.header("Authorization", "Bearer " + TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.modelo", equalTo("Sedans")));
	}
	
	@Test
	@Order(8)
	@Transactional
	void deveDeletarCarroComSucesso() throws Exception {
		
		Long id = carroService.findAll().get(0).getId();
		
		assertEquals(1, carroRepository.count());
		
		mockMvc.perform(delete(CAR_ENDPOINT + "/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(0, carroRepository.count());
	}

	@Test
	@Order(9)
	void devePaginarUmaListaDeCarrosComSucesso() throws Exception {
		
		var carros = carroService.findAll();
		Page<Carro> page = new PageImpl<>(carros, PageRequest.of(0, 10), carros.size());
		
		mockMvc.perform(get(CAR_ENDPOINT + "/pages")
				.header("Authorization", "Bearer " + TOKEN))
		 		.andExpect(status().isOk())
		 		.andReturn();
		
		assertEquals(page.getContent().size(), carros.size());
	}
}
