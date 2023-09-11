package com.wamk.uber.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.entities.Carro;
import com.wamk.uber.services.CarroService;

@RestController
@RequestMapping("/carros")
public class CarroController {

	@Autowired
	private CarroService carroService;
	
	@GetMapping
	public List<Carro> findAll(){
		List<Carro> list = carroService.findAll();
		return list;
	}
	
	@GetMapping("/{id}")
	public Carro findById(@PathVariable Long id){
		Carro carro = carroService.findById(id);
		return carro;
	}
	
	@PostMapping
	public ResponseEntity<Carro> registrarCarro(@RequestBody Carro carro){
		carroService.save(carro);
		return ResponseEntity.ok(carro);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Carro> atualizarCarro(@RequestBody Carro carro, 
			@PathVariable Long id){
		carro.setId(id);
		carroService.save(carro);
		return ResponseEntity.ok(carro);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> atualizarCarro(@PathVariable Long id){
		carroService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
