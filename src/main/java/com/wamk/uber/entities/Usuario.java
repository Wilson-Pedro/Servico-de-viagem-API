package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity(name = "TB_USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Length(min = 2, max = 100)
	private String nome;

	@NotBlank
	@Length(min = 5, max = 100)
	private String telefone;
	
	@NotNull
	private TipoUsuario tipoUsuario;
	
	@NotNull
	private UsuarioStatus usuarioStatus;

	public Usuario() {
	}

	public Usuario(Long id, String nome, String telefone, TipoUsuario tipoUsuario, UsuarioStatus usuarioStatus) {
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.tipoUsuario = tipoUsuario;
		this.usuarioStatus = usuarioStatus;
	}
	
	public boolean estaAtivo() {
		return usuarioStatus == UsuarioStatus.ATIVO;
	}
	
	public boolean estaDesativado() {
		return usuarioStatus == UsuarioStatus.DESATIVADO;
	}
	
	public boolean estaCorrendo() {
		return usuarioStatus == UsuarioStatus.CORRENDO;
	}
	
	public void ativar() {
		setUsuarioStatus(UsuarioStatus.ATIVO);
	}
	
	public void desativar() {
		setUsuarioStatus(UsuarioStatus.DESATIVADO);
	}
	
	public void correr() {
		setUsuarioStatus(UsuarioStatus.CORRENDO);
	}

	public void setUsuarioStatus(UsuarioStatus usuarioStatus) {
		this.usuarioStatus = usuarioStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
}
