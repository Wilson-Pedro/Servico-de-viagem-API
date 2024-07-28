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

import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RequestMapping("/viagens")
public interface ViagemAPI {

	@GetMapping(produces = "application/json", path = "")
	ResponseEntity<List<ViagemDTO>> findAll();
	
	@GetMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<ViagemDTO> findById(@PathVariable Long id);
	
	@GetMapping(produces = "application/json", path = "/pages")
	ResponseEntity<Page<ViagemDTO>> findById(Pageable pageable);
	
	@PostMapping(produces = "application/json", path = "/")
	ResponseEntity<ViagemDTO> registrarUsuario(@RequestBody @Valid ViagemInputDTO viagemInputDTO);
	
	@PutMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<ViagemDTO> atualiziar(@RequestBody @Valid ViagemInputDTO viagemInputDTO, 
			@PathVariable @NotNull @Positive Long id);
	
	@DeleteMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<Void> deleteById(@PathVariable Long id);
	
	@DeleteMapping(produces = "application/json", path = "/{id}/cancelar")
	ResponseEntity<Void> cancelar(@PathVariable Long id);
	
	@PatchMapping(produces = "application/json", path = "/{id}/finalizar")
	ResponseEntity<Void> finishTrip(@PathVariable Long id);
}
