package com.wamk.uber.exceptions;

public class TelefoneJaExisteException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public TelefoneJaExisteException(final String telefone) {
		super(String.format("Telefone: [%s] já existe", telefone));
	}
}
