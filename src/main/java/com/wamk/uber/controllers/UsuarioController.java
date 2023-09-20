package com.wamk.uber.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.mapper.UsuarioMapper;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.services.UsuarioService;
import com.wamk.uber.services.ViagemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ViagemService viagemService;
	
	@Autowired
	private UsuarioMapper usuarioMapper;
	
	@GetMapping
	public List<UsuarioDTO> findAll(){
		List<Usuario> list = usuarioService.findAll();
		return list.stream().map(x -> usuarioMapper.toDTO(x)).toList();
	}
	
	@GetMapping("/{id}")
	public UsuarioDTO findById(@PathVariable Long id){
		var usuario = usuarioService.findById(id);
		return usuarioMapper.toDTO(usuario);
	}
	
	@PostMapping
	public UsuarioDTO registrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO){
		var usuario = usuarioService.save(usuarioDTO);
		return usuarioMapper.toDTO(usuario);
	}
	
	@PutMapping("/{id}")
	public UsuarioDTO atualiziar(@RequestBody @Valid UsuarioDTO usuarioDTO, 
			@PathVariable @NotNull @Positive Long id){
		var usuario = usuarioService.atualizarCadastro(usuarioDTO, id);
		return usuarioMapper.toDTO(usuario);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable @NotNull @Positive Long id){
		usuarioService.delete(id);
	}
	
	@PostMapping("/solicitacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void solicitandoViagem(@RequestBody @Valid SolicitarViagemDTO solicitacao) {
		viagemService.solicitandoViagem(solicitacao);
	}
}
