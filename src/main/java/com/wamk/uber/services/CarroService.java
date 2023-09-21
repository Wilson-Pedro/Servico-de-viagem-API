package com.wamk.uber.services;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.mapper.CarroMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.repositories.CarroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class CarroService {

	private final CarroRepository carroRepository;

	private final CarroMapper carroMapper;

	public CarroService(CarroRepository carroRepository, CarroMapper carroMapper) {
		this.carroRepository = carroRepository;
		this.carroMapper = carroMapper;
	}

	@Transactional
	public Carro save(CarroDTO carroDTO) {
		validarSave(carroDTO);
		return carroRepository.save(carroMapper.toEntity(carroDTO));
	}

	public List<Carro> findAll() {
		return carroRepository.findAll();
	}

	public Carro findById(Long id) {
		return carroRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}

	@Transactional
	public void delete(Long id) {
		carroRepository.delete(carroRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}

	public Carro atualizarCadastro(CarroDTO carroDTO,Long id) {
		validarUpdate(carroDTO, id);
		return carroRepository.findById(id)
				.map(carro -> {
					carro.setAno(carroDTO.getAno());
					carro.setModelo(carroDTO.getModelo());
					carro.setPlaca(carroDTO.getPlaca());
					return carroRepository.save(carro);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}

	public void validarSave(CarroDTO carro) {
		if(carroRepository.existsByPlaca(carro.getPlaca())) {
			throw new PlacaExistenteException("Placa já cadastrada.");
		}
	}

	public void validarUpdate(CarroDTO carroDTO, Long id) {
		var carro = carroRepository.findByPlaca(carroDTO.getPlaca());
		if(carroRepository.existsByPlaca(carroDTO.getPlaca()) && !Objects.equals(carro.getId(), id)) {
			throw new PlacaExistenteException("Placa já cadastrada.");
		}
	}
}
