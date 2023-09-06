package com.wamk.uber.entities;

public class Passageiro extends Usuario{
	private static final long serialVersionUID = 1L;

	public Passageiro() {
		super();
	}

	public Passageiro(Long id, String nome, String telefone) {
		super(id, nome, telefone);
	}
}
