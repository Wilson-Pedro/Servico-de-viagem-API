package com.wamk.uber.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.mapper.CarroMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.repositories.CarroRepository;

import jakarta.validation.Valid;

@Service
public class CarroService {

	@Autowired
	private CarroRepository carroRepository;
	
	@Autowired
	private CarroMapper carroMapper;
	
	@Transactional
	public CarroDTO save(CarroDTO carroDTO) {
		validarSave(carroDTO);
		return carroMapper.toDTO(carroRepository.save(carroMapper.toEntity(carroDTO)));
	}

	public List<CarroDTO> findAll() {
		List<Carro> list =  carroRepository.findAll();
		return list.stream().map(x -> carroMapper.toDTO(x)).toList();
	}

	public CarroDTO findById(Long id) {
		return carroRepository.findById(id)
				.map(carroMapper::toDTO)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}

	@Transactional
	public void delete(Long id) {
		carroRepository.delete(carroRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}

	public CarroDTO atualizarCadastro(@Valid CarroDTO carroDTO,Long id) {
		validarUpdate(carroDTO, id);
		return carroRepository.findById(id)
				.map(carro -> {
					carro.setAno(carroDTO.getAno());
					carro.setModelo(carroDTO.getModelo());
					carro.setPlaca(carroDTO.getPlaca());
					return carroMapper.toDTO(carroRepository.save(carro));
				}).orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}
	
	public void validarSave(CarroDTO carro) {
		if(carroRepository.existsByPlaca(carro.getPlaca())) {
			throw new PlacaExistenteException("Placa já cadastrada.");
		}
	}
	
	public void validarUpdate(CarroDTO carro, Long id) {
		if(carroRepository.existsByPlaca(carro.getPlaca()) && carro.getId() == id) {
			throw new PlacaExistenteException("Placa já cadastrada.");
		}
	}
}
