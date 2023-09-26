package com.wamk.uber.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.entities.Usuario;

import jakarta.validation.constraints.NotBlank;

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

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
}
