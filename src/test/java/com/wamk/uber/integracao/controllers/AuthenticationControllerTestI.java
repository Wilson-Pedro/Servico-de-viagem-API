package com.wamk.uber.integracao.controllers;

import static com.wamk.uber.LoginUniversal.LOGIN;
import static com.wamk.uber.LoginUniversal.PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.enums.roles.UserRole;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTestI {
	
	private static String ENDPOINT = "/auth";
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TokenService tokenService;

	@Test
	@Order(1)
	void deveRegistraUsuarioComSucesso() throws Exception {
		userRepository.deleteAll();
		assertEquals(0, userRepository.count());
		
		RegistroDTO registroDTO = new RegistroDTO(LOGIN, PASSWORD, UserRole.ADMIN);

		String jsonRequest = objectMapper.writeValueAsString(registroDTO);
		
		mockMvc.perform(post(ENDPOINT + "/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isCreated());
		
		assertEquals(1, userRepository.count());
		assertEquals(UserRole.ADMIN, registroDTO.getRole());
		
	}
	
	@Test
	@Order(2)
	void deveRealizarLoginComSucesso() throws Exception {
		AuthenticationDTO authenticationDTO = new AuthenticationDTO(LOGIN, PASSWORD);
		
		String jsonRequest = objectMapper.writeValueAsString(authenticationDTO);
		
		mockMvc.perform(post(ENDPOINT + "/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk());
	}
}
