package com.wamk.uber.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.enums.FormaDePagamento;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TB_VIAGEM")
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "passageiro_id")
	private Passageiro passageiro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "motorista_id")
	private Motorista motorista;
	
	@NotNull
	private FormaDePagamento formaDePagamento;

	public Viagem() {
	}

	public Viagem(Long id, String origem, String destino, String tempoDeViagem, 
			Passageiro passageiro, Motorista motorista, FormaDePagamento formaDePagamento) {
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.tempoDeViagem = tempoDeViagem;
		this.passageiro = passageiro;
		this.motorista = motorista;
		this.formaDePagamento = formaDePagamento;
	}
	
	public Viagem(SolicitarViagemDTO solicitacao) {
		origem = solicitacao.getOrigem();
		destino = solicitacao.getDestino();
		tempoDeViagem = solicitacao.getDestino();
		formaDePagamento = solicitacao.getFormaDePagamento();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getTempoDeViagem() {
		return tempoDeViagem;
	}

	public void setTempoDeViagem(String tempoDeViagem) {
		this.tempoDeViagem = tempoDeViagem;
	}

	public Passageiro getPassageiro() {
		return passageiro;
	}

	public void setPassageiro(Passageiro passageiro) {
		this.passageiro = passageiro;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
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
