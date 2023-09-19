package com.wamk.uber.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.wamk.uber.enums.FormaDePagamento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TB_SOLICITAR_VIAGEM")
public class SolicitarViagem implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String origem;
	
	@NotBlank
	private String destino;
	
	@NotBlank
	private String nomePassageiro;
	
	@NotNull
	private BigDecimal valorAPagar;
	
	@NotNull
	private FormaDePagamento formaDePagamento;

	public SolicitarViagem() {
	}

	public SolicitarViagem(Long id, String origem, String destino, String nomePassageiro, BigDecimal valorAPagar,
			FormaDePagamento formaDePagamento) {
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.nomePassageiro = nomePassageiro;
		this.valorAPagar = valorAPagar;
		this.formaDePagamento = formaDePagamento;
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

	public BigDecimal getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(BigDecimal valorAPagar) {
		this.valorAPagar = valorAPagar;
	}

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
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
