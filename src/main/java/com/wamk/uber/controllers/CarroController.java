package com.wamk.uber.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.mapper.CarroMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.services.CarroService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/carros")
public class CarroController {
	
	private final CarroService carroService;
	
	CarroController(CarroService carroService) {
		this.carroService = carroService;
	}

	@GetMapping
	public ResponseEntity<List<CarroDTO>> findAll(){
		return ResponseEntity.ok(carroService.findAll().stream().map(CarroMapper::toDTO).toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CarroDTO> findById(@PathVariable Long id){
		Carro carro = carroService.findById(id);
		return ResponseEntity.ok(CarroMapper.toDTO(carro));
	}
	
	@GetMapping("/pages")
	public ResponseEntity<Page<CarroDTO>> paginar(Pageable pageable){
		Page<Carro> pages = carroService.findAll(pageable);
		return ResponseEntity.ok(pages.map(CarroDTO::new));
	}
	
	@PostMapping
	public ResponseEntity<CarroDTO> registrarCarro(@RequestBody @Valid CarroDTO carroDTO){
		var carro = carroService.save(carroDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(CarroMapper.toDTO(carro));
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CarroDTO> atualiziar(@RequestBody @Valid CarroDTO carroDTO, 
			@PathVariable @NotNull @Positive Long id){
		var carro = carroService.atualizarCadastro(carroDTO, id);
		return ResponseEntity.ok(CarroMapper.toDTO(carro));
	}
	
	@DeleteMapping("/{id}")	
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		carroService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
