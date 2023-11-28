package com.wamk.uber.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
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
	
	public SolicitarViagemDTO(Viagem viagem) {
		passageiroId = viagem.getPassageiro().getId();
		origem = viagem.getOrigem();
		destino = viagem.getDestino();
		distancia = "5630 metros";
		formaDePagamento = viagem.getFormaDePagamento();
	}
}
