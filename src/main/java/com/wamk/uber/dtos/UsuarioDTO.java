package com.wamk.uber.dtos;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioDTO {

	private Long id;
	
	@NotBlank
	@Length(min = 2, max = 100)
	private String nome;
	
	@NotBlank
	@Length(min = 5, max = 100)
	private String telefone;
	
	@NotNull
	private TipoUsuario tipoUsuario;
	
	public UsuarioDTO() {
	}
	
	public UsuarioDTO(Usuario usuario) {
		id = usuario.getId();
		nome = usuario.getNome();
		telefone = usuario.getTelefone();
		tipoUsuario = usuario.getTipoUsuario();
	}

	public UsuarioDTO(Long id, String nome, String telefone, TipoUsuario tipoUsuario) {
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.tipoUsuario = tipoUsuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
}
