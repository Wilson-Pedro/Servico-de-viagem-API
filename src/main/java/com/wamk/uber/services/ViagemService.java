package com.wamk.uber.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PassageiroCorrendoException;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@Service
public class ViagemService {

	private final ViagemRepository viagemRepository;
	
	private final UsuarioService usuarioService;
	
	private final ViagemMapper viagemMapper;
	
	private final UsuarioRepository usuarioRepository;
	
	public ViagemService(ViagemRepository viagemRepository, UsuarioService usuarioService, ViagemMapper viagemMapper, UsuarioRepository usuarioRepository) {
		this.viagemRepository = viagemRepository;
		this.usuarioService = usuarioService;
		this.viagemMapper = viagemMapper;
		this.usuarioRepository = usuarioRepository;
	}

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

	public Viagem atualizarCadastro(ViagemInputDTO viagemInputDTO,Long id) {
		return viagemRepository.findById(id)
				.map(viagem -> {
					viagem.setOrigem(viagemInputDTO.getOrigem());
					viagem.setDestino(viagemInputDTO.getDestino());
					viagem.setTempoDeViagem(viagemInputDTO.getTempoDeViagem());
					viagem.setFormaDePagamento(FormaDePagamento.toEnum(viagemInputDTO.getFormaDePagamento()));
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
		validarSolicitagem(passageiro);
		Motorista motorista = (Motorista) usuarioService.findByMotoristaStatus(UsuarioStatus.ATIVO);
		motorista.setUsuarioStatus(UsuarioStatus.CORRENDO);
		passageiro.setUsuarioStatus(UsuarioStatus.CORRENDO);
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		viagem.setPassageiro(passageiro);
		viagem.setMotorista((Motorista)motorista);
		viagemRepository.save(viagem);
	}
	
	public void validarSolicitagem(Passageiro passageiro) {
		if(passageiro.getUsuarioStatus().equals(UsuarioStatus.CORRENDO)) {
			throw new PassageiroCorrendoException("");
		}
	}
}
