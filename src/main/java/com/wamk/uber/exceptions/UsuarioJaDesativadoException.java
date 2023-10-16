package com.wamk.uber.exceptions;

public class UsuarioJaDesativadoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioJaDesativadoException(Long id) {
		super(String.format("Usuario com ID = [%s] ja desativado.", id));
	}

}
