package com.wamk.uber.exceptions;

public class UsuarioJaAtivoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioJaAtivoException(Long id) {
		super(String.format("Usuário com Id: [%s] já está ativo!", id));
	}

}
