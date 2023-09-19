package com.wamk.uber.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.wamk.uber.enums.TipoUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_PASSAGEIRO")
@JsonTypeName("passageiro")
public class Passageiro extends Usuario{
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@OneToMany(mappedBy = "passageiro", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Viagem> viagens = new ArrayList<>();

	public Passageiro() {
		super();
	}

	public Passageiro(Long id, String nome, String telefone, TipoUsuario tipoUsuario) {
		super(id, nome, telefone, tipoUsuario);
	}

	public List<Viagem> getViagens() {
		return viagens;
	}
}
