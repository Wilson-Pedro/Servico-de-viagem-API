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
import com.wamk.uber.exceptions.UsuarioDesativadoException;
import com.wamk.uber.exceptions.ViagemJaFinalizadaException;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@Service
public class ViagemService {

	private final ViagemRepository viagemRepository;
	
	private final UsuarioService usuarioService;
	
	private final UsuarioRepository usuarioRepository;
	
	private final ViagemMapper viagemMapper;

	public ViagemService(ViagemRepository viagemRepository, UsuarioService usuarioService,
			UsuarioRepository usuarioRepository, ViagemMapper viagemMapper) {
		this.viagemRepository = viagemRepository;
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
		this.viagemMapper = viagemMapper;
	}

	@Transactional
	public Viagem save(ViagemInputDTO viagemInputDTO) {
		Passageiro passageiro = (Passageiro) usuarioService.findById(viagemInputDTO.getPassageiroId());
		Motorista motorista = (Motorista) usuarioService.findById(viagemInputDTO.getMotoristaId());
		validarSolicitagem(passageiro);
		return viagemRepository.save(viagemMapper.toEntity(viagemInputDTO, passageiro, motorista));
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
		usuarioService.activateUserByViagemId(id);
		viagemRepository.delete(viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}

	@Transactional
	public void solicitandoViagem(SolicitarViagemDTO solicitacao) {
		viagemRepository.save(buildTrip(solicitacao));
	}
	
	private Viagem buildTrip(SolicitarViagemDTO solicitacao) {
		Viagem viagem = new Viagem();
		Passageiro passageiro = (Passageiro) usuarioService.findById(solicitacao.getPassageiroId());
		validarSolicitagem(passageiro);
		Motorista motorista = (Motorista) usuarioService.findByMotoristaStatus(UsuarioStatus.ATIVO);
		motorista.setUsuarioStatus(UsuarioStatus.CORRENDO);
		passageiro.setUsuarioStatus(UsuarioStatus.CORRENDO);
		viagem.setOrigem(solicitacao.getOrigem());
		viagem.setDestino(solicitacao.getDestino());
		viagem.setTempoDeViagem("10 minuutos");
		viagem.setPassageiro(passageiro);
		viagem.setMotorista((Motorista)motorista);
		viagem.setFormaDePagamento(solicitacao.getFormaDePagamento());
		viagem.setViagemStatus(ViagemStatus.NAO_FINALIZADA);
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		return viagem;
	}

	public void validarSolicitagem(Passageiro passageiro) {
		if(passageiro.getUsuarioStatus().equals(UsuarioStatus.CORRENDO)) {
			throw new PassageiroCorrendoException("");
		} else if (passageiro.getUsuarioStatus().equals(UsuarioStatus.DESATIVADO)) {
			throw new UsuarioDesativadoException("");
		}
	}
	
	@Transactional
	public void finishTrip(Long id) {
		Viagem viagem = findById(id);
		usuarioService.activateUserByViagemId(viagem.getId());
		viagem.setViagemStatus(ViagemStatus.FINALIZADA);
		viagemRepository.save(viagem);
	}
	
	@Transactional
	public void cancelTripByUserId(Long Userid) {
		Viagem viagem = findByUser(Userid);
		if(viagem.getViagemStatus().equals(ViagemStatus.FINALIZADA)) {
			throw new ViagemJaFinalizadaException("");
		}
		usuarioService.activateUserByViagemId(viagem.getId());
		viagemRepository.delete(viagem);
	}
	
	@Transactional
	public void cancelTripById(Long id) {
		Viagem viagem = findById(id);
		if(viagem.getViagemStatus().equals(ViagemStatus.FINALIZADA)) {
			throw new ViagemJaFinalizadaException("");
		}
		usuarioService.activateUserByViagemId(viagem.getId());
		viagemRepository.delete(viagem);
	}
	
	public Viagem findByUser(Long id) {
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

	public List<Viagem> getAllTripsByUserId(Long id) {
		List<Viagem> list = findAllTripsByUserId(id);
		return list;
	}
	
	public List<Viagem> findAllTripsByUserId(Long id) {
		var usuario = usuarioService.findById(id);
		List<Viagem> list;
		if(usuario.getTipoUsuario().equals(TipoUsuario.PASSAGEIRO)) {
			list = viagemRepository.findAllByPassageiro((Passageiro) usuario);
		} else {
			list = viagemRepository.findAllByMotorista((Motorista) usuario);
		}
		if(list == null) {
			throw new IllegalArgumentException("");
		}
		return list;
	}
}
