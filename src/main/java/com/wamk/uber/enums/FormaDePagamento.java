package com.wamk.uber.enums;

public enum FormaDePagamento {

	DINHEIRO(1, "Dinherio"),
	DEBITO(2, "Débito"),
	CREDITO(3, "Crédito"),
	PIX(4, "Pix"),
	PAYPAL(5, "PayPal");
	
	private Integer cod;
	private String descricao;
	
	private FormaDePagamento(Integer cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public Integer getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}
}
