package com.wamk.uber.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.enums.FormaDePagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitarViagemDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Long passageiroId;
	
	@NotBlank
	@Length(max = 200)
	private String origem;
	
	@NotBlank
	@Length(max = 200)
	private String destino;
	
	@NotBlank
	@Length(max = 200)
	private String distancia;
	
	@NotNull
	private FormaDePagamento formaDePagamento;
	
	public SolicitarViagemDTO() {
	}

	public SolicitarViagemDTO(Long passageiroId, String origem, String destino, String distancia, FormaDePagamento formaDePagamento) {
		this.passageiroId = passageiroId;
		this.origem = origem;
		this.destino = destino;
		this.distancia = distancia;
		this.formaDePagamento = formaDePagamento;
	}

	public Long getPassageiroId() {
		return passageiroId;
	}

	public void setPassageiroId(Long passageiroId) {
		this.passageiroId = passageiroId;
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

	public String getDistancia() {
		return distancia;
	}

	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}
}
