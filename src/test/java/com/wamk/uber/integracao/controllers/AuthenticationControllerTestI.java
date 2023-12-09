package com.wamk.uber.integracao.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.wamk.uber.LoginUniversal.*;
import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.enums.roles.UserRole;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.UserRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTestI {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	TokenService tokenService;

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

}
