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

import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.services.ViagemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/viagens")
public class ViagemController {

	@Autowired
	private ViagemService viagemService;
	
	@Autowired
	private ViagemMapper viagemMapper;
	
	@GetMapping
	public List<ViagemDTO> findAll(){
		List<Viagem> list = viagemService.findAll();
		return list.stream().map(x -> viagemMapper.toDTO(x)).toList();
	}
	
	@GetMapping("/{id}")
	public ViagemDTO findById(@PathVariable Long id){
		var viagem = viagemService.findById(id);
		return viagemMapper.toDTO(viagem);
	}
	
	@PostMapping
	public ViagemDTO registrarUsuario(@RequestBody @Valid ViagemInputDTO viagemInputDTO){
		var viagem = viagemService.save(viagemInputDTO);
		return viagemMapper.toDTO(viagem);
	}
	
	@PutMapping("/{id}")
	public ViagemDTO atualiziar(@RequestBody @Valid ViagemInputDTO viagemInputDTO, 
			@PathVariable @NotNull @Positive Long id){
		var viagem = viagemService.atualizarCadastro(viagemInputDTO, id);
		return viagemMapper.toDTO(viagem);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable @NotNull @Positive Long id){
		viagemService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
