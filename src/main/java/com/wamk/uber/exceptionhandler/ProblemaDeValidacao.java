package com.wamk.uber.exceptionhandler;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProblemaDeValidacao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String titulo;
	private Integer status;
	private OffsetDateTime dataHora;
	private List<Campo> list = new ArrayList<>();
	
	public ProblemaDeValidacao() {
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

	public List<Campo> getList() {
		return list;
	}

	public void setList(List<Campo> list) {
		this.list = list;
	}
}
