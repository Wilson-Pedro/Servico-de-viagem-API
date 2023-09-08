package com.wamk.uber.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TB_MOTORISTA")
@JsonTypeName("motorista")
@Getter
@Setter
public class Motorista extends Usuario{
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(name = "carro_id")
	@MapsId
	private Carro carro;

	public Motorista() {
		super();
	}

	public Motorista(Long id, String nome, String telefone, Carro carro) {
		super(id, nome, telefone);
		this.carro = carro;
	}
}
