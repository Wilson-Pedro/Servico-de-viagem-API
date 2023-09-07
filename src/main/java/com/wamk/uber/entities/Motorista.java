package com.wamk.uber.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_MOTORISTA")
@JsonTypeName("motorista")
public class Motorista extends Usuario{
	private static final long serialVersionUID = 1L;

	public Motorista() {
		super();
	}

	public Motorista(Long id, String nome, String telefone) {
		super(id, nome, telefone);
	}
}
