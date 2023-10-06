package com.wamk.uber.exceptions;

public class UsuarioCorrendoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioCorrendoException(String msg) {
		super(msg);
	}

}
