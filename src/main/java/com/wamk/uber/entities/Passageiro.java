package com.wamk.uber.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_PASSAGEIRO")
@JsonTypeName("passageiro")
public class Passageiro extends Usuario{
	private static final long serialVersionUID = 1L;

	public Passageiro() {
		super();
	}

	public Passageiro(Long id, String nome, String telefone) {
		super(id, nome, telefone);
	}
}
