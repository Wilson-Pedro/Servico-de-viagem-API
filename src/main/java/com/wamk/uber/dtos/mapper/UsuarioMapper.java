package com.wamk.uber.dtos.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;

@Component
public class UsuarioMapper {

	public static UsuarioDTO toDTO(Usuario usuario) {
		if(usuario == null) {
			return null;
		}
		return new UsuarioDTO(usuario);
	}
	
	public static Usuario toEntity(UsuarioDTO usuarioDTO) {
		if(usuarioDTO == null) {
			return null;
		}
		var usuario = new Usuario();
		if(usuarioDTO.getId() != null) {
			usuario.setId(usuarioDTO.getId());
		}
		usuario.setNome(usuarioDTO.getNome());
		usuario.setTelefone(usuarioDTO.getTelefone());
		usuario.setTipoUsuario(TipoUsuario.toEnum(usuarioDTO.getTipoUsuario()));
		usuario.ativar();
		var user = passageiroOuMotorista(usuario);
		return user;
	}

	private static Usuario passageiroOuMotorista(Usuario usuario) {
		if(usuario.getTipoUsuario().equals(TipoUsuario.PASSAGEIRO)) {
			Passageiro user = new Passageiro();
			BeanUtils.copyProperties(usuario, user);
			return user;
		} else {
			Motorista user = new Motorista();
			BeanUtils.copyProperties(usuario, user);
			return user;
		}
	}
}
