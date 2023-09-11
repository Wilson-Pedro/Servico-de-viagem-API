package com.wamk.uber.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.wamk.uber.enums.FormaDePagamento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_SOLICITAR_VIAGEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitarViagem implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String origem;
	
	private String destino;
	
	private String nomePassageiro;
	
	private BigDecimal valorAPagar;
	
	private FormaDePagamento formaDePagamento;

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
		SolicitarViagem other = (SolicitarViagem) obj;
		return Objects.equals(id, other.id);
	}
}
