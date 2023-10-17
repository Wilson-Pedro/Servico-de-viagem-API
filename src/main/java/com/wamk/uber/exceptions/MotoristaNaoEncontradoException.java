package com.wamk.uber.exceptions;

import com.wamk.uber.enums.UsuarioStatus;

public class MotoristaNaoEncontradoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MotoristaNaoEncontradoException(UsuarioStatus status) {
		super(String.format("Motorista com o status [%s] não localizado", status));
	}
}
