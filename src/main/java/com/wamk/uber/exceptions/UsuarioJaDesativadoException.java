package com.wamk.uber.exceptions;

public class UsuarioJaDesativadoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioJaDesativadoException(Long id) {
		super(String.format("Usuário com Id: [%s] já está desativado!", id));
	}
}
