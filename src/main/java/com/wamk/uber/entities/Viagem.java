package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_VIAGEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Viagem implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@Length(min = 2, max = 100)
	private String tempoDeViagem;
	
	@ManyToOne
	@JoinColumn(name = "passageiro_id")
	private Passageiro passageiro;
	
	@ManyToOne
	@JoinColumn(name = "motorista_id")
	private Motorista motorista;

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
		Viagem other = (Viagem) obj;
		return Objects.equals(id, other.id);
	}
}
