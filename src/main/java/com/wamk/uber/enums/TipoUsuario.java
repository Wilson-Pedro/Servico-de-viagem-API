package com.wamk.uber.enums;

public enum TipoUsuario {

	PASSAGEIRO(1, "Passageiro"),
	MOTORISTA(2, "Motorista");
	
	private Integer cod;
	private String descricao;
	
	private TipoUsuario(Integer cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
