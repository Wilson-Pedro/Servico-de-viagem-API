package com.wamk.uber.entities;

public class Motorista extends Usuario{
	private static final long serialVersionUID = 1L;

	public Motorista() {
		super();
	}

	public Motorista(Long id, String nome, String telefone) {
		super(id, nome, telefone);
	}
}
