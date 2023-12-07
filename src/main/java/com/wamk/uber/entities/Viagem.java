package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.enums.converter.FormaDePagamentoConverter;
import com.wamk.uber.enums.converter.ViagemStatusConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(name = "TB_VIAGEM")
@AllArgsConstructor
@Data
public class Viagem implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "passageiro_id")
	private Passageiro passageiro;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "motorista_id")
	private Motorista motorista;
	
	@NotNull
	@Convert(converter = FormaDePagamentoConverter.class)
	private FormaDePagamento formaDePagamento;
	
	@NotNull
	@Convert(converter = ViagemStatusConverter.class)
	private ViagemStatus viagemStatus;

	public Viagem() {
	}
	
	public Viagem(SolicitarViagemDTO solicitacao) {
		origem = solicitacao.getOrigem();
		destino = solicitacao.getDestino();
		tempoDeViagem = solicitacao.getDestino();
		formaDePagamento = solicitacao.getFormaDePagamento();
	}
	
	public boolean estaFinalizada() {
		return viagemStatus == ViagemStatus.FINALIZADA;
	}
	
	public boolean naoEstaFinalizada() {
		return viagemStatus == ViagemStatus.NAO_FINALIZADA;
	}
	
	public void finalizar() {
		setViagemStatus(ViagemStatus.FINALIZADA);
	}
	
	public void naoFinalizar() {
		setViagemStatus(ViagemStatus.NAO_FINALIZADA);
	}

	public void setViagemStatus(ViagemStatus viagemStatus) {
		this.viagemStatus = viagemStatus;
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
		Viagem other = (Viagem) obj;
		return Objects.equals(id, other.id);
	}
}
