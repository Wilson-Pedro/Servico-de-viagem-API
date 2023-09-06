package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

public class SolicitarViagem implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String origem;
	private String destino;
	private String nomePassageiro;
	private String valorAPagar;
	
	public SolicitarViagem() {
	}

	public SolicitarViagem(Long id, String origem, String destino, String nomePassageiro, String valorAPagar) {
		super();
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.nomePassageiro = nomePassageiro;
		this.valorAPagar = valorAPagar;
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

	public String getNomePassageiro() {
		return nomePassageiro;
	}

	public void setNomePassageiro(String nomePassageiro) {
		this.nomePassageiro = nomePassageiro;
	}

	public String getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(String valorAPagar) {
		this.valorAPagar = valorAPagar;
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
		SolicitarViagem other = (SolicitarViagem) obj;
		return Objects.equals(id, other.id);
	}
}
