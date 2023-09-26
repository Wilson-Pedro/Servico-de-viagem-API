package com.wamk.uber.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.mapper.UsuarioMapper;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	private final UsuarioMapper usuarioMapper;
	
	public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioMapper = usuarioMapper;
	}

	@Transactional
	public Usuario save(UsuarioDTO usuarioDTO) {
		return usuarioRepository.save(usuarioMapper.toEntity(usuarioDTO));
	}

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	public Usuario findById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}
	
	public Motorista findByUsuarioStatus(UsuarioStatus status) {
		return usuarioRepository.findByUsuarioStatus(status);
	}

	public Usuario atualizarCadastro(UsuarioDTO usuarioDTO,Long id) {
		return usuarioRepository.findById(id)
				.map(usuario -> {
					usuario.setNome(usuarioDTO.getNome());
					usuario.setTelefone(usuarioDTO.getTelefone());
					usuario.setTipoUsuario(TipoUsuario.toEnum(usuarioDTO.getTipoUsuario()));
					return usuarioRepository.save(usuario);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}
	
	@Transactional
	public void delete(Long id) {
		usuarioRepository.delete(usuarioRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}
	
}
