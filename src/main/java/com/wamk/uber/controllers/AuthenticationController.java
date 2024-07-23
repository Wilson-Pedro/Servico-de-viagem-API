package com.wamk.uber.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.dtos.records.LoginResponseDTO;
import com.wamk.uber.entities.user.User;
import com.wamk.uber.infra.security.TokenService;
import com.wamk.uber.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	final AuthenticationManager authenticationManager;
	
	final UserRepository userRepository;

	final TokenService tokenService;
	
	public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegistroDTO data) {
		if(this.userRepository.findByLogin(data.getLogin()) != null) {
			return ResponseEntity.badRequest().build();
		}
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
		User newUser = new User(data.getLogin(), encryptedPassword, data.getRole());
		
		this.userRepository.save(newUser);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
