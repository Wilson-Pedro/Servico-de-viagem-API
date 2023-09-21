package com.wamk.uber.controllers;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.mapper.CarroMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.services.CarroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {

	private final CarroService carroService;

	private final CarroMapper carroMapper;

	public CarroController(CarroService carroService, CarroMapper carroMapper) {
		this.carroService = carroService;
		this.carroMapper = carroMapper;
	}

	@GetMapping
	public ResponseEntity<List<CarroDTO>> findAll() {
		List<Carro> list = carroService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(list.stream().map(carroMapper::toDTO).toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CarroDTO> findById(@PathVariable Long id) {
		Carro carro = carroService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(carroMapper.toDTO(carro));
	}

	@PostMapping
	public ResponseEntity<CarroDTO> registrarCarro(@RequestBody @Valid CarroDTO carroDTO) {
		var carro = carroService.save(carroDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(carroMapper.toDTO(carro));

	}

	@PutMapping("/{id}")
	public ResponseEntity<CarroDTO> atualizar(@RequestBody @Valid CarroDTO carroDTO, @PathVariable Long id) {
		var carro = carroService.atualizarCadastro(carroDTO, id);
		return ResponseEntity.status(HttpStatus.CREATED).body(carroMapper.toDTO(carro));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		carroService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
