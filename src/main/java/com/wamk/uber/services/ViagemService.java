package com.wamk.uber.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
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
	
	private final MyObjectMapper modelMapper;

	private final ViagemRepository viagemRepository;
	
	private final UsuarioService usuarioService;
	
	private final UsuarioRepository usuarioRepository;

	public ViagemService(ViagemRepository viagemRepository, UsuarioService usuarioService,
			UsuarioRepository usuarioRepository, MyObjectMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.viagemRepository = viagemRepository;
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	public Viagem save(ViagemInputDTO viagemInputDTO) {
		Passageiro passageiro = (Passageiro) usuarioService.findById(viagemInputDTO.getPassageiroId());
		Motorista motorista = (Motorista) usuarioService.findById(viagemInputDTO.getMotoristaId());
		//validarSolicitagem(passageiro);
		return viagemRepository.save(modelMapper.toViagemEntity(viagemInputDTO, passageiro, motorista));
	}

	@Transactional
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
		usuarioService.ativarUsuarioPorViagemId(id);
		Viagem viagem = viagemRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(id));
		viagemRepository.delete(viagem);
	}

	@Transactional
	public void solicitandoViagem(SolicitarViagemDTO solicitacao) {
		viagemRepository.save(construirViagem(solicitacao));
	}
	
	private Viagem construirViagem(SolicitarViagemDTO solicitacao) {
		Viagem viagem = new Viagem();
		Passageiro passageiro = (Passageiro) usuarioService.findById(solicitacao.getPassageiroId());
		validarSolicitagem(passageiro);
		Motorista motorista = (Motorista) usuarioService.buscarMotoristaPorStatus(UsuarioStatus.ATIVO);
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
	public void finalizarViagem(Long id) {
		Viagem viagem = findById(id);
		usuarioService.ativarUsuarioPorViagemId(viagem.getId());
		viagem.finalizar();
		viagemRepository.save(viagem);
	}
	
	@Transactional
	public void cancelarViagemPorUserId(Long Userid) {
		
		Viagem viagem = acharViagemPorUserId(Userid);
		
		if(viagem.estaFinalizada()) {
			throw new ViagemJaFinalizadaException("");
		}
		usuarioService.ativarUsuarioPorViagemId(viagem.getId());
		viagemRepository.delete(viagem);
	}
	
	@Transactional
	public void cancelarViagemPorViagemId(Long id) {
		
		Viagem viagem = findById(id);
		
		if(viagem.estaFinalizada()) {
			throw new ViagemJaFinalizadaException("");
		}
		usuarioService.ativarUsuarioPorViagemId(viagem.getId());
		viagemRepository.delete(viagem);
	}
	
	public Viagem acharViagemPorUserId(Long UserId) {
		
		Usuario usuario = usuarioService.findById(UserId);
		Viagem viagem = new Viagem();
		
		if(usuario.getTipoUsuario().equals(TipoUsuario.PASSAGEIRO)) {
			viagem = acharViagemPorPassageiro((Passageiro) usuario);
		} else {
			viagem = acharViagemPorMotorista((Motorista) usuario);
		}
		if(viagem == null) {
			throw new EntidadeNaoEncontradaException(UserId);
		}
//		if(viagem == null) {
//			throw new ViagemJaFinalizadaException("");
//		}
		return viagem;
	}
	
	public Viagem acharViagemPorPassageiro(Passageiro passageiro) {
		return viagemRepository.findByPassageiro(passageiro);
	}
	
	public Viagem acharViagemPorMotorista(Motorista motorista) {
		return viagemRepository.findByMotorista(motorista);
	}

	public List<Viagem> buscarTodasAsViagensPorUserId(Long UserId) {
		List<Viagem> list = acharTodasAsViagensPorUserId(UserId);
		return list;
	}
	
	public List<Viagem> acharTodasAsViagensPorUserId(Long UserId) {
		
		var usuario = usuarioService.findById(UserId);
		List<Viagem> list;
		
		if(usuario.getTipoUsuario().equals(TipoUsuario.PASSAGEIRO)) {
			list = viagemRepository.findAllByPassageiro((Passageiro) usuario);
		} else {
			list = viagemRepository.findAllByMotorista((Motorista) usuario);
		} 
		return list;
	}
}
