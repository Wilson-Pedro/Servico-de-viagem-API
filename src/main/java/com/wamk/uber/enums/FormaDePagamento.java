package com.wamk.uber.enums;

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
}
