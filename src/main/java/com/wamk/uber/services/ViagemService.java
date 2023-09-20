package com.wamk.uber.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.repositories.ViagemRepository;

import jakarta.validation.Valid;

@Service
public class ViagemService {

	@Autowired
	private ViagemRepository viagemRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ViagemMapper viagemMapper;
	
	@Transactional
	public Viagem save(ViagemInputDTO viagemInputDTO) {
		return viagemRepository.save(viagemMapper.toEntity(viagemInputDTO));
	}

	public List<Viagem> findAll() {
		return  viagemRepository.findAll();
	}

	public Viagem findById(Long id) {
		return viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}

	public Viagem atualizarCadastro(@Valid ViagemInputDTO viagemInputDTO,Long id) {
		return viagemRepository.findById(id)
				.map(viagem -> {
					viagem.setOrigem(viagemInputDTO.getOrigem());
					viagem.setDestino(viagemInputDTO.getDestino());
					viagem.setTempoDeViagem(viagemInputDTO.getTempoDeViagem());
					viagem.setFormaDePagamento(viagemInputDTO.getFormaDePagamento());
					return viagemRepository.save(viagem);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}
	
	@Transactional
	public void delete(Long id) {
		viagemRepository.delete(viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}

	public void solicitandoViagem(SolicitarViagemDTO solicitacao) {
		Viagem viagem = new Viagem(solicitacao);
		Passageiro passageiro = (Passageiro) usuarioService.findById(solicitacao.getPassageiroId());
		Motorista motorista = (Motorista) usuarioService.findById(2L);
		viagem.setPassageiro(passageiro);
		viagem.setMotorista(motorista);
		viagemRepository.save(viagem);
	}
}
