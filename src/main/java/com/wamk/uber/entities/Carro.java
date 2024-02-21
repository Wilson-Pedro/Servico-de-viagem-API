package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.dtos.CarroDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity(name = "TB_CARRO")
@Data
public class Carro implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Length(min = 2, max = 100)
	private String modelo;
	
	@NotNull
	private Integer ano;
	
	@NotBlank
	@Length(min = 8, max = 50)
	private String placa;
	
	@OneToOne
	@JoinColumn(name = "motorista_id")
	private Motorista motorista;
	
	public Carro() {
	}

	public Carro(Long id, String modelo, Integer ano, String placa) {
		this.id = id;
		this.modelo = modelo;
		this.ano = ano;
		this.placa = placa;
	}
	
	public Carro(String modelo, Integer ano, String placa, Motorista motorista) {
		this.modelo = modelo;
		this.ano = ano;
		this.placa = placa;
		this.motorista = motorista;
	}
	
	public Carro(CarroDTO carroDTO) {
		id = carroDTO.getId();
		modelo = carroDTO.getModelo();
		ano = carroDTO.getAno();
		placa = carroDTO.getPlaca();
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carro other = (Carro) obj;
		return Objects.equals(id, other.id);
	}



	public Carro(Long id, @NotBlank @Length(min = 2, max = 100) String modelo, @NotNull Integer ano,
			@NotBlank @Length(min = 8, max = 50) String placa, Motorista motorista) {
		super();
		this.id = id;
		this.modelo = modelo;
		this.ano = ano;
		this.placa = placa;
		this.motorista = motorista;
	}
}
