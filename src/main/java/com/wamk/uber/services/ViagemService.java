package com.wamk.uber.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.PassageiroCorrendoException;
import com.wamk.uber.exceptions.ViagemJaFinalizadaException;
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
	
	public Page<Viagem> findAll(Pageable pageable) {
		return viagemRepository.findAll(pageable);
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
		usuarioService.updateUsuarioStatus(id);
		viagemRepository.delete(viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}

	@Transactional
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
		viagem.setViagemStatus(ViagemStatus.NAO_FINALIZADA);
		viagemRepository.save(viagem);
	}
	
	public void validarSolicitagem(Passageiro passageiro) {
		if(passageiro.getUsuarioStatus().equals(UsuarioStatus.CORRENDO)) {
			throw new PassageiroCorrendoException("");
		}
	}
	
	@Transactional
	public void finishTrip(Long id) {
		Viagem viagem = findByUserId(id);
		usuarioService.updateUsuarioStatus(viagem.getId());
		viagem.setViagemStatus(ViagemStatus.FINALIZADA);
		viagemRepository.save(viagem);
	}
	
	@Transactional
	public void cancelTrip(Long id) {
		Viagem viagem = findByUserId(id);
		if(viagem.getViagemStatus().equals(ViagemStatus.FINALIZADA)) {
			throw new ViagemJaFinalizadaException("");
		}
		usuarioService.updateUsuarioStatus(viagem.getId());
		viagemRepository.delete(viagem);
	}
	
	public Viagem findByUserId(Long id) {
		Usuario usuario = usuarioService.findById(id);
		Viagem viagem = new Viagem();
		if(usuario.getTipoUsuario().equals(TipoUsuario.PASSAGEIRO)) {
			viagem = viagemRepository.findByPassageiro((Passageiro) usuario);
		} else {
			viagem = viagemRepository.findByMotorista((Motorista) usuario);
		}
		if(viagem == null) {
			throw new ViagemJaFinalizadaException("");
		}
		return viagem;
	}
}
