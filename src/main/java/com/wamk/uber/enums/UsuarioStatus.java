package com.wamk.uber.enums;

public enum UsuarioStatus {

	ATIVO("Ativo"),
	CORRENDO("Correndo"),
	DESATIVADO("Desativado");
	
	private String descricao;
	
	private UsuarioStatus(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
