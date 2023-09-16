package com.wamk.uber.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.services.UsuarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public List<UsuarioDTO> findAll(){
		List<UsuarioDTO> list = usuarioService.findAll();
		return list;
	}
	
	@GetMapping("/{id}")
	public UsuarioDTO findById(@PathVariable Long id){
		var usuario = usuarioService.findById(id);
		var usuarioDTO = new UsuarioDTO(usuario);
		return usuarioDTO;
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO){
		usuarioDTO = usuarioService.salvarCadastro(usuarioDTO);
		return ResponseEntity.ok(usuarioDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDTO> update(@RequestBody @Valid UsuarioDTO usuarioDTO, 
			@PathVariable @NotNull @Positive Long id){
		usuarioDTO = usuarioService.atualizarCadastro(usuarioDTO, id);
		return ResponseEntity.ok(usuarioDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable @NotNull @Positive Long id){
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
