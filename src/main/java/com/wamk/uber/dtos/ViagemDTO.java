package com.wamk.uber.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import com.wamk.uber.entities.Viagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ViagemDTO implements Serializable{
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

	@NotBlank
	private String nomePassageiro;
	
	@NotBlank
	private String nomeMotorista;
	
	@NotNull
	private String formaDePagamento;
	
	public ViagemDTO() {
	}

	public ViagemDTO (Viagem viagem) {
		BeanUtils.copyProperties(viagem, this);
		nomePassageiro = viagem.getPassageiro().getNome();
		nomeMotorista = viagem.getMotorista().getNome();
		formaDePagamento = viagem.getFormaDePagamento().getDescricao();
	}

	public ViagemDTO(Long id, String origem,String destino, String tempoDeViagem, String nomePassageiro, 
			String nomeMotorista, String formaDePagamento) {
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.tempoDeViagem = tempoDeViagem;
		this.nomePassageiro = nomePassageiro;
		this.nomeMotorista = nomeMotorista;
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

	public String getNomePassageiro() {
		return nomePassageiro;
	}

	public void setNomePassageiro(String nomePassageiro) {
		this.nomePassageiro = nomePassageiro;
	}

	public String getNomeMotorista() {
		return nomeMotorista;
	}

	public void setNomeMotorista(String nomeMotorista) {
		this.nomeMotorista = nomeMotorista;
	}

	public String getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(String formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}
}
