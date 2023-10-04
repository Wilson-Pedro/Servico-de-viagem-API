package com.wamk.uber.enums;

import java.util.stream.Stream;

public enum ViagemStatus {

	NAO_FINALIZADA("Não Finalizada"),
	FINALIZADA("Finalizada");
	
	private String descricao;

	private ViagemStatus(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static ViagemStatus toEnum(String descricao) {
		return Stream.of(ViagemStatus.values())
				.filter(status -> status.getDescricao().equals(descricao))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException
						("Status inválido: " + descricao));
	}
}
