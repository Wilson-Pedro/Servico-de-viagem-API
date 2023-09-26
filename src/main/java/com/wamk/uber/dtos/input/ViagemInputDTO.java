package com.wamk.uber.dtos.input;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ViagemInputDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	@Length(max = 200)
	private String origem;
	
	@NotBlank
	@Length(max = 200)
	private String destino;

	@NotBlank
	@Length(max = 100)
	private String tempoDeViagem;

	@NotNull
	private Long passageiroId;
	
	@NotNull
	private Long motoristaId;
	
	@NotNull
	private String formaDePagamento;
	
	public ViagemInputDTO() {
	}

	public ViagemInputDTO(Long id, String origem, String destino, String tempoDeViagem,
			Long passageiroId, Long motoristaId, String formaDePagamento) {
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.tempoDeViagem = tempoDeViagem;
		this.passageiroId = passageiroId;
		this.motoristaId = motoristaId;
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

	public String getTempoDeViagem() {
		return tempoDeViagem;
	}

	public void setTempoDeViagem(String tempoDeViagem) {
		this.tempoDeViagem = tempoDeViagem;
	}

	public Long getPassageiroId() {
		return passageiroId;
	}

	public void setPassageiroId(Long passageiroId) {
		this.passageiroId = passageiroId;
	}

	public Long getMotoristaId() {
		return motoristaId;
	}

	public void setMotoristaId(Long motoristaId) {
		this.motoristaId = motoristaId;
	}

	public String getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(String formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}
}
