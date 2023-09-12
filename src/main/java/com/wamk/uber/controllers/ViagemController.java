package com.wamk.uber.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wamk.uber.entities.Viagem;
import com.wamk.uber.services.ViagemService;

@RestController
@RequestMapping("/viagens")
public class ViagemController {

	@Autowired
	private ViagemService viagemService;
	
	@GetMapping
	public List<Viagem> findAll(){
		List<Viagem> list = viagemService.findAll();
		return list;
	}
	
	@GetMapping("/{id}")
	public Viagem findById(@PathVariable Long id){
		Viagem viagem = viagemService.findById(id);
		return viagem;
	}
	
//	@PostMapping
//	public ResponseEntity<Viagem> registrarViagem(@RequestBody Viagem viagem){
//		viagemService.save(viagem);
//		return ResponseEntity.ok(viagem);
//	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Viagem> update(@RequestBody Viagem viagem, 
			@PathVariable Long id){
		viagemService.findById(id);
		viagem.setId(id);
		viagemService.save(viagem);
		return ResponseEntity.ok(viagem);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		viagemService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
