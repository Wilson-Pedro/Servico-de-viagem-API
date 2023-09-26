package com.wamk.uber.enums;

import java.util.stream.Stream;

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
	
	public static UsuarioStatus toEnum(String descricao) {
		return Stream.of(UsuarioStatus.values())
				.filter(status -> status.getDescricao().equals(descricao))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException
						("Tipo de Usuário inválido: " + descricao));
	}
}
