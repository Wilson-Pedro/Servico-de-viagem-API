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
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.services.ViagemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/viagens")
public class ViagemController {
	
	private final MyObjectMapper modelMapper;

	private final ViagemService viagemService;
	
	ViagemController(ViagemService viagemService, MyObjectMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.viagemService = viagemService;
	}

	@GetMapping
	public ResponseEntity<List<ViagemDTO>> findAll(){
		List<Viagem> list = viagemService.findAll();
		return ResponseEntity.ok(modelMapper.converterList(list, ViagemDTO.class));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ViagemDTO> findById(@PathVariable Long id){
		var viagem = viagemService.findById(id);
		return ResponseEntity.ok(modelMapper.converter(viagem, ViagemDTO.class));
	}
	
	@GetMapping("/pages")
	public ResponseEntity<Page<ViagemDTO>> findById(Pageable pageable){
		Page<Viagem> pages = viagemService.findAll(pageable);
		return ResponseEntity.ok(pages.map(ViagemDTO::new));
	}
	
	@PostMapping("/")
	public ResponseEntity<ViagemDTO> registrarUsuario(@RequestBody @Valid ViagemInputDTO viagemInputDTO){
		var viagem = viagemService.save(viagemInputDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(
				modelMapper.converter(viagem, ViagemDTO.class));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ViagemDTO> atualiziar(@RequestBody @Valid ViagemInputDTO viagemInputDTO, 
			@PathVariable @NotNull @Positive Long id){
		var viagem = viagemService.atualizarCadastro(viagemInputDTO, id);
		return ResponseEntity.ok(modelMapper.converter(viagem, ViagemDTO.class));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		viagemService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}/finalizar")
	public ResponseEntity<Void> finishTrip(@PathVariable Long id){
		viagemService.finalizarViagem(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}/cancelar")
	public ResponseEntity<Void> cancelar(@PathVariable Long id){
		viagemService.cancelarViagemPorViagemId(id);
		return ResponseEntity.noContent().build();
	}
}
