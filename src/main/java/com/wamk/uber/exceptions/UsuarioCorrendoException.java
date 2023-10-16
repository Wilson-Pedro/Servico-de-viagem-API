package com.wamk.uber.exceptions;

public class UsuarioCorrendoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioCorrendoException(Long id) {
		super(String.format("Usuario com o ID = [%s] ja esta correndo.", id));
	}

}
