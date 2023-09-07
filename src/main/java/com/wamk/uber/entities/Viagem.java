package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_VIAGEM")
public class Viagem implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String origem;
	private String destino;
	private String tempoDeViagem;
	
	public Viagem() {
	}

	public Viagem(Long id, String origem, String destino, String tempoDeViagem) {
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.tempoDeViagem = tempoDeViagem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getTempoDeViagem() {
		return tempoDeViagem;
	}

	public void setTempoDeViagem(String tempoDeViagem) {
		this.tempoDeViagem = tempoDeViagem;
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
		Viagem other = (Viagem) obj;
		return Objects.equals(id, other.id);
	}
}
