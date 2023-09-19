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

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.mapper.CarroMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.services.CarroService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/carros")
@Validated
public class CarroController {

	@Autowired
	private CarroService carroService;
	
	@Autowired
	private CarroMapper carroMapper;
	
	@GetMapping
	public List<CarroDTO> findAll(){
		List<Carro> list = carroService.findAll();
		return list.stream().map(x -> carroMapper.toDTO(x)).toList();
	}
	
	@GetMapping("/{id}")
	public CarroDTO findById(@PathVariable Long id){
		Carro carro = carroService.findById(id);
		return carroMapper.toDTO(carro);
	}
	
	@PostMapping
	public CarroDTO registrarCarro(@RequestBody @Valid CarroDTO carroDTO){
		var carro = carroService.save(carroDTO);
		return carroMapper.toDTO(carro);
		
	}
	
	@PutMapping("/{id}")
	public CarroDTO atualiziar(@RequestBody @Valid CarroDTO carroDTO, 
			@PathVariable @NotNull @Positive Long id){
		var carro = carroService.atualizarCadastro(carroDTO, id);
		return carroMapper.toDTO(carro);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable @NotNull @Positive Long id){
		carroService.delete(id);
	}
}
