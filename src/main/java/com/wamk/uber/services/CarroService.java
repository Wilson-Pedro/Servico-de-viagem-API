package com.wamk.uber.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.entities.Carro;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.repositories.CarroRepository;

@Service
public class CarroService {

	@Autowired
	private CarroRepository carroRepository;
	
	@Transactional
	public void save(Carro carro) {
		carroRepository.save(carro);
	}

	public List<Carro> findAll() {
		return carroRepository.findAll();
	}

	public Carro findById(Long id) {
		return carroRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}

	@Transactional
	public void deleteById(Long id) {
		findById(id);
		carroRepository.deleteById(id);
	}

	public void validarCarro(Carro carro) {
		if(carroRepository.existsByPlaca(carro.getPlaca())) {
			throw new PlacaExistenteException("Placa já cadastrada.");
		}
	}
}
