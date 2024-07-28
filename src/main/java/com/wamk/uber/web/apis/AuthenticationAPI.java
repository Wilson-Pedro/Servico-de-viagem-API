package com.wamk.uber.web.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wamk.uber.dtos.RegistroDTO;
import com.wamk.uber.dtos.records.AuthenticationDTO;
import com.wamk.uber.dtos.records.LoginResponseDTO;

import jakarta.validation.Valid;

@RequestMapping("/auth")
public interface AuthenticationAPI {

	@PostMapping(produces = "application/json", path = "/login")
	ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data);
	
	@PostMapping(produces = "application/json", path = "/register")
	ResponseEntity<Void> register(@RequestBody @Valid RegistroDTO data);
}
