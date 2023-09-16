package com.wamk.uber.exceptions;

public class TelefoneExistenteException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public TelefoneExistenteException(String msg) {
		super(msg);
	}
}
