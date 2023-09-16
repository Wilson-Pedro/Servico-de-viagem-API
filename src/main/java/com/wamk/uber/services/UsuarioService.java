package com.wamk.uber.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.repositories.UsuarioRepository;

import jakarta.validation.Valid;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public void save(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	public List<UsuarioDTO> findAll() {
		List<Usuario> list =  usuarioRepository.findAll();
		return list.stream().map(x -> new UsuarioDTO(x)).toList();
	}

	public Usuario findById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}

	@Transactional
	public void deleteById(Long id) {
		findById(id);
		usuarioRepository.deleteById(id);
	}

	@Transactional
	public UsuarioDTO salvarCadastro(UsuarioDTO usuarioDTO) {
		var usuario = new Usuario();
		BeanUtils.copyProperties(usuarioDTO, usuario);
		save(usuario);
		usuarioDTO.setId(usuario.getId());
		return usuarioDTO;
	}

	public UsuarioDTO atualizarCadastro(@Valid UsuarioDTO usuarioDTO,Long id) {
		usuarioDTO.setId(id);
		usuarioDTO = salvarCadastro(usuarioDTO);
		return usuarioDTO;
	}
}
