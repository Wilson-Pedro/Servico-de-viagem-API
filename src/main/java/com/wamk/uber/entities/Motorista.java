package com.wamk.uber.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.wamk.uber.enums.TipoUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
	private Carro carro;
	
	@JsonIgnore
	@OneToMany(mappedBy = "motorista")
	private List<Viagem> viagens = new ArrayList<>();

	public Motorista() {
		super();
	}

	public Motorista(Long id, String nome, String telefone, TipoUsuario tipoUsuario, Carro carro) {
		super(id, nome, telefone, tipoUsuario);
		this.carro = carro;
	}

	
}
