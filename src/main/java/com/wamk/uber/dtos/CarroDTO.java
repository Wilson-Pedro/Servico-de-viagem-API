package com.wamk.uber.dtos;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wamk.uber.dtos.input.CarroMinDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarroDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank
	@Length(min = 2, max = 100)
	private String modelo;
	
	@NotNull
	private Integer ano;
	
	@NotBlank
	@Length(min = 8, max = 50)
	private String placa;
	
	private Motorista motorista;

	public CarroDTO() {
	}
	
	public CarroDTO(Long id, String modelo, Integer ano, String placa) {
		this.id = id;
		this.modelo = modelo;
		this.ano = ano;
		this.placa = placa;
	}
	
	public CarroDTO(Carro carro) {
		id = carro.getId();
		modelo = carro.getModelo();
		ano = carro.getAno();
		placa = carro.getPlaca();
		motorista = carro.getMotorista();
	}
	
	public CarroDTO(CarroMinDTO carro) {
		id = carro.getId();
		modelo = carro.getModelo();
		ano = carro.getAno();
		placa = carro.getPlaca();
	}
}
