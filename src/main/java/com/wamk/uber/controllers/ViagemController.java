package com.wamk.uber.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.services.ViagemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/viagens")
public class ViagemController {

	private final ViagemService viagemService;

	private final ViagemMapper viagemMapper;
	
	public ViagemController(ViagemService viagemService, ViagemMapper viagemMapper) {
		this.viagemService = viagemService;
		this.viagemMapper = viagemMapper;
	}

	@GetMapping
	public ResponseEntity<List<ViagemDTO>> findAll(){
		List<Viagem> list = viagemService.findAll();
		return ResponseEntity.ok(list.stream().map(x -> viagemMapper.toDTO(x)).toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ViagemDTO> findById(@PathVariable Long id){
		var viagem = viagemService.findById(id);
		return ResponseEntity.ok(viagemMapper.toDTO(viagem));
	}
	
	@PostMapping
	public ResponseEntity<ViagemDTO> registrarUsuario(@RequestBody @Valid ViagemInputDTO viagemInputDTO){
		var viagem = viagemService.save(viagemInputDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(viagemMapper.toDTO(viagem));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ViagemDTO> atualiziar(@RequestBody @Valid ViagemInputDTO viagemInputDTO, 
			@PathVariable @NotNull @Positive Long id){
		var viagem = viagemService.atualizarCadastro(viagemInputDTO, id);
		return ResponseEntity.ok(viagemMapper.toDTO(viagem));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		viagemService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
