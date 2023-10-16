package com.wamk.uber.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public EntidadeNaoEncontradaException(final Long id) {
		super(String.format("Entidade com o ID: [%s] não foi encontrada.", id));
	}

}
