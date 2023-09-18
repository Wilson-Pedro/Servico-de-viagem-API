package com.wamk.uber.dtos.mapper;

import org.springframework.stereotype.Component;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Usuario;

@Component
public class UsuarioMapper {

	public UsuarioDTO toDTO(Usuario usuario) {
		if(usuario == null) {
			return null;
		}
		return new UsuarioDTO(usuario);
	}
	
	public Usuario toEntity(UsuarioDTO usuarioDTO) {
		if(usuarioDTO == null) {
			return null;
		}
		var usuario = new Usuario();
		if(usuarioDTO.getId() != null) {
			usuario.setId(usuarioDTO.getId());
		}
		usuario.setNome(usuarioDTO.getNome());
		usuario.setTelefone(usuarioDTO.getTelefone());
		usuario.setTipoUsuario(usuarioDTO.getTipoUsuario());
		return usuario;
	}
}
