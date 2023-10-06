package com.wamk.uber.exceptions;

public class UsuarioJaAtivoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioJaAtivoException(String msg) {
		super(msg);
	}

}
