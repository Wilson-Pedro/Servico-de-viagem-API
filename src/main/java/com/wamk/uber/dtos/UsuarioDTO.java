package com.wamk.uber.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.entities.Usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	@Length(min = 2, max = 100)
	private String nome;
	
	@NotBlank
	@Length(min = 5, max = 100)
	private String telefone;
	
	@NotBlank
	private String tipoUsuario;
	
	public UsuarioDTO() {
	}
	
	public UsuarioDTO(Usuario usuario) {
		id = usuario.getId();
		nome = usuario.getNome();
		telefone = usuario.getTelefone();
		tipoUsuario = usuario.getTipoUsuario().getDescricao();
	}

	public UsuarioDTO(Long id, String nome, String telefone, String tipoUsuario) {
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.tipoUsuario = tipoUsuario;
	}
}
