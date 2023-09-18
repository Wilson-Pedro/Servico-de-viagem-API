package com.wamk.uber.dtos;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wamk.uber.entities.Motorista;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
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
public class CarroDTO {
	
	private Long id;
	
	@NotNull
	@NotBlank
	@Length(min = 2, max = 100)
	private String modelo;
	
	@NotNull
	private Integer ano;
	
	@NotNull
	@NotBlank
	@Length(min = 8, max = 50)
	private String placa;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "carro")
	private Motorista motorista;
}
