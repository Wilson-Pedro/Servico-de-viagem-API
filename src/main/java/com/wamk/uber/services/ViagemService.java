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

	ViagemService(ViagemRepository viagemRepository, UsuarioService usuarioService,
			UsuarioRepository usuarioRepository) {
		this.viagemRepository = viagemRepository;
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	public Viagem save(ViagemInputDTO viagemInputDTO) {
		Passageiro passageiro = (Passageiro) usuarioService.findById(viagemInputDTO.getPassageiroId());
		Motorista motorista = (Motorista) usuarioService.findById(viagemInputDTO.getMotoristaId());
		//validarSolicitagem(passageiro);
		return viagemRepository.save(ViagemMapper.toEntity(viagemInputDTO, passageiro, motorista));
	}

	public List<Viagem> findAll() {
		return  viagemRepository.findAll();
	}

	public Viagem findById(Long id) {
		return viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(id));
	}
	
	public Page<Viagem> findAll(Pageable pageable) {
		return viagemRepository.findAll(pageable);
	}

	public Viagem atualizarCadastro(ViagemInputDTO viagemInputDTO, Long id) {
		return viagemRepository.findById(id)
				.map(viagem -> {
					viagem.setOrigem(viagemInputDTO.getOrigem());
					viagem.setDestino(viagemInputDTO.getDestino());
					viagem.setTempoDeViagem(viagemInputDTO.getTempoDeViagem());
					viagem.setFormaDePagamento(FormaDePagamento.toEnum(viagemInputDTO.getFormaDePagamento()));
					return viagemRepository.save(viagem);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException(id));
	}
	
	@Transactional
	public void delete(Long id) {
		usuarioService.activateUserByViagemId(id);
		Viagem viagem = viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(id));
		viagemRepository.delete(viagem);
	}

	@Transactional
	public void solicitandoViagem(SolicitarViagemDTO solicitacao) {
		viagemRepository.save(buildTrip(solicitacao));
	}
	
	private Viagem buildTrip(SolicitarViagemDTO solicitacao) {
		Viagem viagem = new Viagem();
		Passageiro passageiro = (Passageiro) usuarioService.findById(solicitacao.getPassageiroId());
		validarSolicitagem(passageiro);
		Motorista motorista = (Motorista) usuarioService.findMotoristaByStatus(UsuarioStatus.ATIVO);
		motorista.correr();
		passageiro.correr();
		viagem.setOrigem(solicitacao.getOrigem());
		viagem.setDestino(solicitacao.getDestino());
		viagem.setTempoDeViagem("10 minuutos");
		viagem.setPassageiro(passageiro);
		viagem.setMotorista((Motorista)motorista);
		viagem.setFormaDePagamento(solicitacao.getFormaDePagamento());
		viagem.naoFinalizar();
		usuarioRepository.saveAll(List.of(passageiro, motorista));
		return viagem;
	}

	public void validarSolicitagem(Passageiro passageiro) {
		if(passageiro.estaCorrendo()) {
			throw new PassageiroCorrendoException("");
		} else if (passageiro.estaDesativado()) {
			throw new UsuarioDesativadoException("");
		}
	}
	
	@Transactional
	public void finishTrip(Long id) {
		Viagem viagem = findById(id);
		usuarioService.activateUserByViagemId(viagem.getId());
		viagem.finalizar();
		viagemRepository.save(viagem);
	}
	
	@Transactional
	public void cancelTripByUserId(Long Userid) {
		
		Viagem viagem = findViagemByUserId(Userid);
		
		if(viagem.estaFinalizada()) {
			throw new ViagemJaFinalizadaException("");
		}
		usuarioService.activateUserByViagemId(viagem.getId());
		viagemRepository.delete(viagem);
	}
	
	@Transactional
	public void cancelTripById(Long id) {
		
		Viagem viagem = findById(id);
		
		if(viagem.estaFinalizada()) {
			throw new ViagemJaFinalizadaException("");
		}
		usuarioService.activateUserByViagemId(viagem.getId());
		viagemRepository.delete(viagem);
	}
	
	public Viagem findViagemByUserId(Long UserId) {
		
		Usuario usuario = usuarioService.findById(UserId);
		Viagem viagem = new Viagem();
		
		if(usuario.getTipoUsuario().equals(TipoUsuario.PASSAGEIRO)) {
			viagem = findViagemByPassageiro((Passageiro) usuario);
		} else {
			viagem = findViagemByMotorista((Motorista) usuario);
		}
		if(viagem == null) {
			throw new EntidadeNaoEncontradaException(UserId);
		}
//		if(viagem == null) {
//			throw new ViagemJaFinalizadaException("");
//		}
		return viagem;
	}
	
	public Viagem findViagemByPassageiro(Passageiro passageiro) {
		return viagemRepository.findByPassageiro(passageiro);
	}
	
	public Viagem findViagemByMotorista(Motorista motorista) {
		return viagemRepository.findByMotorista(motorista);
	}

	public List<Viagem> getAllTripsByUserId(Long UserId) {
		List<Viagem> list = findAllTripsByUserId(UserId);
		return list;
	}
	
	public List<Viagem> findAllTripsByUserId(Long UserId) {
		
		var usuario = usuarioService.findById(UserId);
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
