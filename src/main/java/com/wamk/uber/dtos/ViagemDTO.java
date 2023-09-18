package com.wamk.uber.dtos;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import com.wamk.uber.entities.Viagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViagemDTO {

	private Long id;
	
	@NotNull
	@NotBlank
	@Length(max = 200)
	private String origem;
	
	@NotNull
	@NotBlank
	@Length(max = 200)
	private String destino;
	
	@NotNull
	@NotBlank
	@Length(max = 100)
	private String tempoDeViagem;

	@NotNull
	@NotBlank
	private String nomePassageiro;
	
	@NotNull
	@NotBlank
	private String nomeMotorista;
	
	public ViagemDTO (Viagem viagem) {
		BeanUtils.copyProperties(viagem, this);
		nomePassageiro = viagem.getPassageiro().getNome();
		nomeMotorista = viagem.getMotorista().getNome();
	}
}
