package com.wamk.uber.exceptionhandler;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class Problema implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String titulo;
	private Integer status;
	private OffsetDateTime dataHora;
	
	public Problema() {
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public OffsetDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(OffsetDateTime dataHora) {
		this.dataHora = dataHora;
	}
}
