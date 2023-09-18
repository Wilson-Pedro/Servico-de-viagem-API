package com.wamk.uber.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.repositories.ViagemRepository;

import jakarta.validation.Valid;

@Service
public class ViagemService {

	@Autowired
	private ViagemRepository viagemRepository;
	
	@Autowired
	private ViagemMapper viagemMapper;
	
	@Transactional
	public ViagemDTO save(ViagemInputDTO viagemInputDTO) {
		return viagemMapper.toDTO(viagemRepository.save(viagemMapper.toEntity(viagemInputDTO)));
	}

	public List<ViagemDTO> findAll() {
		List<Viagem> list =  viagemRepository.findAll();
		return list.stream().map(x -> viagemMapper.toDTO(x)).toList();
	}

	public ViagemDTO findById(Long id) {
		return viagemRepository.findById(id)
				.map(viagemMapper::toDTO)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}

	@Transactional
	public void delete(Long id) {
		viagemRepository.delete(viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}

	public ViagemDTO atualizarCadastro(@Valid ViagemInputDTO viagemInputDTO,Long id) {
		return viagemRepository.findById(id)
				.map(viagem -> {
					viagem.setOrigem(viagemInputDTO.getOrigem());
					viagem.setDestino(viagemInputDTO.getDestino());
					viagem.setTempoDeViagem(viagemInputDTO.getTempoDeViagem());
					return viagemMapper.toDTO(viagemRepository.save(viagem));
				}).orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}
	
}
