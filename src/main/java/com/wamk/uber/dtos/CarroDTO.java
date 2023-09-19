package com.wamk.uber.dtos;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wamk.uber.entities.Motorista;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CarroDTO {
	
	private Long id;
	
	@NotBlank
	@Length(min = 2, max = 100)
	private String modelo;
	
	@NotNull
	private Integer ano;
	
	@NotBlank
	@Length(min = 8, max = 50)
	private String placa;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "carro")
	private Motorista motorista;

	public CarroDTO() {
	}

	public CarroDTO(Long id, String modelo, Integer ano, String placa, Motorista motorista) {
		this.id = id;
		this.modelo = modelo;
		this.ano = ano;
		this.placa = placa;
		this.motorista = motorista;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
}
