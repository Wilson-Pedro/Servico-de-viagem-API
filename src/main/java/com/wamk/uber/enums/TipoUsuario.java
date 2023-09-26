package com.wamk.uber.enums;

import java.util.stream.Stream;

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
	
	@Override
	public String toString() {
		return descricao;
	}
	
	public static TipoUsuario toEnum(String descricao) {
		return Stream.of(TipoUsuario.values())
				.filter(tipo -> tipo.getDescricao().equals(descricao))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException
						("Tipo de Usuário inválido: " + descricao));
	}

}
