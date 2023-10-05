package com.wamk.uber.exceptions;

public class UsuarioDesativadoException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public UsuarioDesativadoException(String msg) {
		super(msg);
	}

}
