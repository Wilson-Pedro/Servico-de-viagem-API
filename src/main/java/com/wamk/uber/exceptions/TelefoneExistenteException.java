package com.wamk.uber.exceptions;

public class TelefoneExistenteException extends RuntimeException{

	public TelefoneExistenteException(final String telefone) {
		super(String.format("Telefone [%s] não encontrado.", telefone));
	}
}
