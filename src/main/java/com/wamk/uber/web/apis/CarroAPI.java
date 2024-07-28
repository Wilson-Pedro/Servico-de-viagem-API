package com.wamk.uber.web.apis;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.input.CarroMinDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RequestMapping("/carros")
public interface CarroAPI {
	
	@GetMapping(produces = "application/json", path = "")
	ResponseEntity<List<CarroMinDTO>> findAll();

	@GetMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<CarroDTO> findById(@PathVariable Long id);
	
	@GetMapping(produces = "application/json", path = "/pages")
	ResponseEntity<Page<CarroMinDTO>> paginar(Pageable pageable);
	
	@PostMapping(produces = "application/json", path = "/")
	ResponseEntity<CarroDTO> registrarCarro(@RequestBody @Valid CarroMinDTO inputDTO);
	
	@PutMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<CarroDTO> atualiziar(@RequestBody @Valid CarroDTO carroDTO, 
			@PathVariable @NotNull @Positive Long id);
	
	@DeleteMapping(produces = "application/json", path = "/{id}")
	ResponseEntity<Void> deleteById(@PathVariable Long id);
}
