package com.wamk.uber.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import com.wamk.uber.entities.Viagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
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
	
	@NotNull
	private String viagemStatus;
	
	public ViagemDTO() {
	}

	public ViagemDTO (Viagem viagem) {
		BeanUtils.copyProperties(viagem, this);
		nomePassageiro = viagem.getPassageiro().getNome();
		nomeMotorista = viagem.getMotorista().getNome();
		formaDePagamento = viagem.getFormaDePagamento().getDescricao();
		viagemStatus = viagem.getViagemStatus().getDescricao();
	}
}
