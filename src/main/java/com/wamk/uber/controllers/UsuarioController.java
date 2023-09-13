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

import com.wamk.uber.entities.Usuario;
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
	public List<Usuario> findAll(){
		List<Usuario> list = usuarioService.findAll();
		return list;
	}
	
	@GetMapping("/{id}")
	public Usuario findById(@PathVariable Long id){
		Usuario usuario = usuarioService.findById(id);
		return usuario;
	}
	
	@PostMapping
	public ResponseEntity<Usuario> registrarUsuario(@RequestBody @Valid Usuario usuario){
		usuarioService.save(usuario);
		return ResponseEntity.ok(usuario);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> update(@RequestBody @Valid Usuario usuario, 
			@PathVariable @NotNull @Positive Long id){
		usuarioService.findById(id);
		usuario.setId(id);
		usuarioService.save(usuario);
		return ResponseEntity.ok(usuario);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable @NotNull @Positive Long id){
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
