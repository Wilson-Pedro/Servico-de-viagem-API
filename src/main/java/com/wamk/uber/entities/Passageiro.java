package com.wamk.uber.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TB_PASSAGEIRO")
@JsonTypeName("passageiro")
@Getter
@Setter
public class Passageiro extends Usuario{
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@OneToMany(mappedBy = "passageiro")
	private List<Viagem> viagens = new ArrayList<>();

	public Passageiro() {
		super();
	}

	public Passageiro(Long id, String nome, String telefone) {
		super(id, nome, telefone);
	}
}
