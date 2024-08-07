package com.wamk.uber.services.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.MotoristaNaoEncontradoException;
import com.wamk.uber.exceptions.TelefoneJaExisteException;
import com.wamk.uber.exceptions.UsuarioCorrendoException;
import com.wamk.uber.exceptions.UsuarioJaAtivoException;
import com.wamk.uber.exceptions.UsuarioJaDesativadoException;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.interfaces.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private final MyObjectMapper modelMapper;

	private final UsuarioRepository usuarioRepository;
	
	private final ViagemRepository viagemRepository;

	public UsuarioServiceImpl(MyObjectMapper modelMapper, UsuarioRepository usuarioRepository,
			ViagemRepository viagemRepository) {
		this.modelMapper = modelMapper;
		this.usuarioRepository = usuarioRepository;
		this.viagemRepository = viagemRepository;
	}

	@Transactional
	public Usuario save(UsuarioDTO usuarioDTO) {
		validarCadastroUsuario(usuarioDTO);
		return usuarioRepository.save(modelMapper.toUsuarioEntity(usuarioDTO));
	}

	@Transactional
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
	
	public Page<Usuario> findAll(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}

	public Usuario findById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(id));
	}
	
	public Motorista buscarMotoristaPorStatus(UsuarioStatus status) {
		return usuarioRepository.findAllByUsuarioStatus(status)
				.stream()
				.filter(x -> x.getUsuarioStatus().equals(status))
				.findFirst()
				.orElseThrow(() -> new MotoristaNaoEncontradoException(status));
	}

	public Usuario atualizarCadastro(UsuarioDTO usuarioDTO, Long id) {
		validarAtualizacaoDoUsuario(usuarioDTO, id);
		return usuarioRepository.findById(id)
				.map(usuario -> {
					usuario.setNome(usuarioDTO.getNome());
					usuario.setTelefone(usuarioDTO.getTelefone());
					usuario.setTipoUsuario(TipoUsuario.toEnum(usuarioDTO.getTipoUsuario()));
					return usuarioRepository.save(usuario);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException(id));
	}
	
	@Transactional
	public void delete(Long id) {
		usuarioRepository.delete(usuarioRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(id)));
	}
	
	public void validarCadastroUsuario(UsuarioDTO usuarioDTO) {
		
		final var telefone = usuarioDTO.getTelefone();
		
		if(usuarioRepository.existsByTelefone(telefone)) {
			throw new TelefoneJaExisteException(telefone);
		}
	}
	
	public void validarAtualizacaoDoUsuario(UsuarioDTO usuarioDTO, Long id) {
		
		final var telefone = usuarioDTO.getTelefone();
		
		if(usuarioRepository.existsByTelefone(telefone) 
				&& !Objects.equals(usuarioDTO.getId(), id)) {
			throw new TelefoneJaExisteException(telefone);
		}
	}
				
	public void ativarUsuarioPorViagemId(Long id) {
		Viagem viagem = viagemRepository.findById(id).get();
		Passageiro passageiro = (Passageiro) findById(viagem.getPassageiro().getId());
		Motorista motorista = (Motorista) findById(viagem.getMotorista().getId());
		passageiro.ativar();
		motorista.ativar();
		usuarioRepository.saveAll(List.of(passageiro, motorista));
	}

	public void desativarUsuario(Long id) {
		
		var usuario = findById(id);
		
		if(usuario.estaDesativado()) {
			throw new UsuarioJaDesativadoException(id);
		} else if (usuario.estaCorrendo()) {
			
			throw new UsuarioCorrendoException(id);
		}
		
		usuario.desativar();
		inserirNoBancoDeDados(usuario);
	}

	public void ativarUsuario(Long id) {
		
		var usuario = findById(id);
		
		if(usuario.estaAtivo()) {
			throw new UsuarioJaAtivoException(id);
		}
		
		usuario.ativar();
		inserirNoBancoDeDados(usuario);
	}
	
	public void inserirNoBancoDeDados(Usuario usuario) {
		usuarioRepository.save(usuario);
	}
}