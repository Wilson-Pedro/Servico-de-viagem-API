package com.wamk.uber.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.input.CarroMinDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.repositories.CarroRepository;

@Service
public class CarroService {
	
	private final MyObjectMapper modelMapper;

	private final CarroRepository carroRepository;
	
	private final UsuarioService usuarioService;
	
	public CarroService(MyObjectMapper modelMapper, CarroRepository carroRepository, UsuarioService usuarioService) {
		this.modelMapper = modelMapper;
		this.carroRepository = carroRepository;
		this.usuarioService = usuarioService;
	}

	@Transactional
	public Carro save(CarroMinDTO inputDTO) {
		validarSave(inputDTO);
		CarroDTO carroDTO = toCarroDto(inputDTO);
		return carroRepository.save(modelMapper.converter(carroDTO, Carro.class));
	}

	public List<Carro> findAll() {
		return  carroRepository.findAll();
	}
	
	public Page<Carro> findAll(Pageable pageable) {
		return carroRepository.findAll(pageable);
	}

	public Carro findById(Long id) {
		Carro carro = carroRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(id));
		return carro;
	}

	public void delete(Long id) {
		carroRepository.delete(carroRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(id)));
	}

	public Carro atualizarCadastro(CarroDTO carroDTO,Long id) {
		validarUpdate(carroDTO, id);
		return carroRepository.findById(id)
				.map(carro -> {
					carro.setAno(carroDTO.getAno());
					carro.setModelo(carroDTO.getModelo());
					carro.setPlaca(carroDTO.getPlaca());
					return carroRepository.save(carro);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException(id));
	}
	
	public void validarSave(CarroMinDTO carro) {
		if(carroRepository.existsByPlaca(carro.getPlaca())) {
			throw new PlacaExistenteException("Placa já cadastrada.");
		}
	}
	
	public void validarUpdate(CarroDTO carroDTO, Long id) {
		if(carroRepository.existsByPlaca(carroDTO.getPlaca()) && 
				!Objects.equals(carroDTO.getId(), id)) {
			throw new PlacaExistenteException("Placa já cadastrada.");
		}
	}
	
	public CarroDTO toCarroDto(CarroMinDTO carroInputDTO) {
		
		if(carroInputDTO == null) {
			return null;
		}
		
		Long motoristaId = carroInputDTO.getMotoristaId();
		
		Motorista motorista = (Motorista) usuarioService.findById(motoristaId);
		CarroDTO carroDto = new CarroDTO(carroInputDTO);
		carroDto.setMotorista(motorista);
		
		return carroDto;
	}
}
