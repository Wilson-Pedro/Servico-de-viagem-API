package com.wamk.uber.dtos.input;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.entities.Carro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarroInputDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	@Length(max = 30)
	private String modelo;
	
	@NotNull
	private Integer ano;
	
	@NotBlank
	@Length(min = 8, max = 50)
	private String placa;
	
	@NotNull
	private Long motoristaId;
	
	public CarroInputDTO() {
	}

	public CarroInputDTO(Long id, String modelo, Integer ano, String placa, Long motoristaId) {
		this.id = id;
		this.modelo = modelo;
		this.ano = ano;
		this.placa = placa;
		this.motoristaId = motoristaId;
	}
	
	public CarroInputDTO(Carro carro) {
		id = carro.getId();
		modelo = carro.getModelo();
		ano = carro.getAno();
		placa = carro.getPlaca();
		motoristaId = carro.getMotorista().getId();
	}
}
