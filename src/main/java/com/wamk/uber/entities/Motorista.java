package com.wamk.uber.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity(name = "TB_MOTORISTA")
@JsonTypeName("motorista")
public class Motorista extends Usuario{
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@OneToOne(mappedBy = "motorista")
	private Carro carro;
	
	@JsonIgnore
	@OneToMany(mappedBy = "motorista", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Viagem> viagens = new ArrayList<>();

	public Motorista() {
	}

	public Motorista(Long id, String nome, String telefone, TipoUsuario tipoUsuario, UsuarioStatus usuarioStatus) {
		super(id, nome, telefone, tipoUsuario, usuarioStatus);
	}

	public Carro getCarro() {
		return carro;
	}

	public void setCarro(Carro carro) {
		this.carro = carro;
	}

	public List<Viagem> getViagens() {
		return viagens;
	}
}
