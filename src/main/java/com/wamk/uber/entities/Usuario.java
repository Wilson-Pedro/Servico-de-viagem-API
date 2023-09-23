package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.converter.TipoUsuarioConverter;
import com.wamk.uber.enums.converter.UsuarioStatusConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TB_USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
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
	@Convert(converter = TipoUsuarioConverter.class)
	private TipoUsuario tipoUsuario;
	
	@NotNull
	@Convert(converter = UsuarioStatusConverter.class)
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

	public UsuarioStatus getUsuarioStatus() {
		return usuarioStatus;
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
