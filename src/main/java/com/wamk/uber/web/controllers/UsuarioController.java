package com.wamk.uber.web.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.services.interfaces.UsuarioService;
import com.wamk.uber.services.interfaces.ViagemService;
import com.wamk.uber.web.apis.UsuarioAPI;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
public class UsuarioController implements UsuarioAPI{
	
	private final MyObjectMapper modelMapper;

	private final UsuarioService usuarioService;
	
	private final ViagemService viagemService;

	public UsuarioController(MyObjectMapper modelMapper, UsuarioService usuarioService, ViagemService viagemService) {
		this.modelMapper = modelMapper;
		this.usuarioService = usuarioService;
		this.viagemService = viagemService;
	}

	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> findAll() {
		List<Usuario> list = usuarioService.findAll();
		List<UsuarioDTO> dtos = modelMapper.converterList(list, UsuarioDTO.class);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/pages")
	public ResponseEntity<Page<UsuarioDTO>> paginar(Pageable pageable) {
		Page<Usuario> pages = usuarioService.findAll(pageable);
		return ResponseEntity.ok(pages.map(UsuarioDTO::new));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
		var usuario = usuarioService.findById(id);
		var dto = modelMapper.converter(usuario, UsuarioDTO.class);
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/{id}/viagens")
	public ResponseEntity<List<ViagemDTO>> findAllTripsByUserId(@PathVariable Long id) {
		List<Viagem> list = viagemService.buscarTodasAsViagensPorUserId(id);
		List<ViagemDTO> dtos = modelMapper.converterList(list, ViagemDTO.class);
		return ResponseEntity.ok(dtos);
	}
	
	@PostMapping("/")
	public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {
		var usuario = usuarioService.save(usuarioDTO);
		var dto = new UsuarioDTO(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDTO> atualiziar(@RequestBody @Valid UsuarioDTO usuarioDTO, 
			@PathVariable @NotNull @Positive Long id) {
		var usuario = usuarioService.atualizarCadastro(usuarioDTO, id);
		return ResponseEntity.ok(modelMapper.toUsuarioDTO(usuario));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable @NotNull @Positive Long id) {
		usuarioService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/solicitacarViagem")
	public ResponseEntity<String> solicitandoViagem(@RequestBody @Valid SolicitarViagemDTO solicitacao) {
		viagemService.solicitandoViagem(solicitacao);
		return ResponseEntity.ok().body("Corrida Solicidada com Sucesso.");
	}
	
	@DeleteMapping("/{usuarioId}/cancelarViagem")
	public ResponseEntity<Void> cancelTrip(@PathVariable Long usuarioId) {
		viagemService.cancelarViagemPorUserId(usuarioId);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}/ativar")
	public ResponseEntity<String> ativarUsuario(@PathVariable Long id) {
		usuarioService.ativarUsuario(id);
		return ResponseEntity.ok().body("Usuario ativado com sucesso.");
	}
	
	@PatchMapping("/{id}/desativar")
	public ResponseEntity<String> desativarUsuario(@PathVariable Long id) {
		usuarioService.desativarUsuario(id);
		return ResponseEntity.ok().body("Usuario desativado com sucesso.");
	}
}
