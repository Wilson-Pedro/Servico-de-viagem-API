package com.wamk.uber.enums;

import java.util.stream.Stream;

public enum FormaDePagamento {

	DINHEIRO("Dinherio"),
	DEBITO("Débito"),
	CREDITO("Crédito"),
	PIX("Pix"),
	PAYPAL("PayPal");
	
	private String descricao;
	
	private FormaDePagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static FormaDePagamento toEnum(String descricao) {
		return Stream.of(FormaDePagamento.values())
				.filter(pagamento -> pagamento.getDescricao().equals(descricao))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException
						("Formato de Pagamento inválido: " + descricao));
	}
}
