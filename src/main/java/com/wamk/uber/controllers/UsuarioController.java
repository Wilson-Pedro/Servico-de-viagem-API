package com.wamk.uber.controllers;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.mapper.UsuarioMapper;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.services.UsuarioService;
import com.wamk.uber.services.ViagemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;
	
	private final ViagemService viagemService;

	UsuarioController(UsuarioService usuarioService, ViagemService viagemService) {
		this.usuarioService = usuarioService;
		this.viagemService = viagemService;
	}

	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> findAll(){
		List<Usuario> list = usuarioService.findAll();
		return ResponseEntity.ok(list.stream().map(x -> UsuarioMapper.toDTO(x)).toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id){
		var usuario = usuarioService.findById(id);
		return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
	}
	
	@GetMapping("/{id}/viagens")
	public ResponseEntity<List<ViagemDTO>> findAllTripsByUserId(@PathVariable Long id){
		List<Viagem> list = viagemService.getAllTripsByUserId(id);
		return ResponseEntity.ok(list.stream().map(x -> ViagemMapper.toDTO(x)).toList());
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO){
		var usuario = usuarioService.save(usuarioDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(usuario));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDTO> atualiziar(@RequestBody @Valid UsuarioDTO usuarioDTO, 
			@PathVariable @NotNull @Positive Long id){
		var usuario = usuarioService.atualizarCadastro(usuarioDTO, id);
		return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
	}
	
	@GetMapping("/pages")
	public ResponseEntity<Page<UsuarioDTO>> paginar(Pageable pageable){
		Page<Usuario> pages = usuarioService.findAll(pageable);
		return ResponseEntity.ok(pages.map(UsuarioDTO::new));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable @NotNull @Positive Long id){
		usuarioService.delete(id);
	}
	
	@PostMapping("/solicitacarViagem")
	public ResponseEntity<Void> solicitandoViagem(@RequestBody @Valid SolicitarViagemDTO solicitacao) {
		viagemService.solicitandoViagem(solicitacao);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{usuarioId}/cancelarViagem")
	public ResponseEntity<Void> cancelTrip(@PathVariable Long usuarioId){
		viagemService.cancelTripByUserId(usuarioId);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}/ativar")
	public ResponseEntity<Void> ativarUsuario(@PathVariable Long id){
		usuarioService.ativarUsuario(id);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}/desativar")
	public ResponseEntity<Void> desativarUsuario(@PathVariable Long id){
		usuarioService.desativarUsuario(id);
		return ResponseEntity.noContent().build();
	}
}
