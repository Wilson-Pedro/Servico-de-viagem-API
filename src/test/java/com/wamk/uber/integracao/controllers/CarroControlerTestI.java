package com.wamk.uber.integracao.controllers;

import static com.wamk.uber.LoginUniversal.LOGIN;
import static com.wamk.uber.LoginUniversal.PASSWORD;
import static com.wamk.uber.LoginUniversal.TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.enums.roles.UserRole;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.CarroRepository;
import com.wamk.uber.repositories.UserRepository;
import com.wamk.uber.services.CarroService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarroControlerTestI {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;

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
	void deveBuscarTodosOsCarrosComSucesso() throws Exception {
		
		mockMvc.perform(get("/carros")
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
		
	}
	
	@Test
	@Order(4)
	void deveBuscarCarroAPartirDoIdComSucesso() throws Exception {
		
		Long id = carroService.findById(1L).getId();
		
		mockMvc.perform(get("/carros/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(5)
	void deveRegistrarCarroComSucesso() throws Exception {
		
		CarroDTO carroDto = new CarroDTO(null, "Toyota", 2022, "HRS-0305");
		
		String jsonRequest = objectMapper.writeValueAsString(carroDto);
		
		mockMvc.perform(post("/carros/")
				.header("Authorization", "Bearer " + TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated())
				.andReturn();
		
		assertEquals(4, carroRepository.count());
	}
	
	@Test
	@Order(6)
	void deveAtualizarCarroComSucesso() throws Exception {
		
		Long id = carroService.findById(2L).getId();
		
		CarroDTO carroDto = new CarroDTO(null, "Sedans", 2023, "JVD-4401");
		
		String jsonRequest = objectMapper.writeValueAsString(carroDto);
		
		mockMvc.perform(put("/carros/{id}", id)
				.header("Authorization", "Bearer " + TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(7)
	void deveDeletarCarroComSucesso() throws Exception {
		
		Long id = carroService.findById(3L).getId();
		
		assertEquals(4, carroRepository.count());
		
		mockMvc.perform(delete("/carros/{id}", id)
				.header("Authorization", "Bearer " + TOKEN))
				.andExpect(status().isNoContent())
				.andReturn();
		
		assertEquals(3, carroRepository.count());
	}
}
