package com.wamk.uber.exceptions;

public class UsuarioJaDesativadoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioJaDesativadoException(String msg) {
		super(msg);
	}

}
