package com.wamk.uber.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.input.CarroMinDTO;
import com.wamk.uber.entities.Carro;

public interface CarroService {

	Carro save(CarroMinDTO inputDTO);
	
	Carro findById(Long id);
	
	Carro atualizarCadastro(CarroDTO carroDTO,Long id);
	
	List<Carro> findAll();
	
	Page<Carro> findAll(Pageable pageable);
	
	void delete(Long id);
}
