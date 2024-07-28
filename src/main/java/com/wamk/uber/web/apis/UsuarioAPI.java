package com.wamk.uber.web.apis;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.ViagemDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RequestMapping("/usuarios")
public interface UsuarioAPI {

	@GetMapping(produces = "application/json", path = "")
	ResponseEntity<List<UsuarioDTO>> findAll();
	
	@GetMapping(produces = "application/json", path = "/pages")
	ResponseEntity<Page<UsuarioDTO>> paginar(Pageable pageable);
	
	@GetMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<UsuarioDTO> findById(@PathVariable Long id);
	
	@GetMapping(produces = "application/json", path = "/{id}/viagens")
	ResponseEntity<List<ViagemDTO>> findAllTripsByUserId(@PathVariable Long id);
	
	@PostMapping(produces = "application/json", path = "/")
	ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO);
	
	@PutMapping(produces = "aplication/json", path = "/{id}")
	ResponseEntity<UsuarioDTO> atualiziar(@RequestBody @Valid UsuarioDTO usuarioDTO, 
			@PathVariable @NotNull @Positive Long id);
	
	@DeleteMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<Void> deleteById(@PathVariable @NotNull @Positive Long id);
	
	@PostMapping(produces = "application/json", path = "/solicitacarViagem")
	ResponseEntity<String> solicitandoViagem(@RequestBody @Valid SolicitarViagemDTO solicitacao);
	
	@DeleteMapping(produces = "application/json", path = "/{usuarioId}/cancelarViagem")
	ResponseEntity<Void> cancelTrip(@PathVariable Long usuarioId);
	
	@PatchMapping(produces = "application/json", path = "/{id}/ativar")
	ResponseEntity<String> ativarUsuario(@PathVariable Long id);
	
	@PatchMapping(produces = "application/json", path = "/{id}/desativar")
	ResponseEntity<String> desativarUsuario(@PathVariable Long id);
}
