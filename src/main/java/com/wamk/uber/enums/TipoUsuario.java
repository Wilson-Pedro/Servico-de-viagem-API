package com.wamk.uber.enums;

public enum TipoUsuario {

	PASSAGEIRO("Passageiro"),
	MOTORISTA("Motorista");

	private String descricao;
	
	private TipoUsuario(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
