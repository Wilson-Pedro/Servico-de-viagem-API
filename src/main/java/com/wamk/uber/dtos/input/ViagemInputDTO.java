package com.wamk.uber.dtos.input;

import org.hibernate.validator.constraints.Length;

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
public class ViagemInputDTO {

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
	private Long passageiroId;
	
	@NotNull
	private Long motoristaId;
}
