package com.wamk.uber.exceptions;

public class MotoristaNaoEncontradoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MotoristaNaoEncontradoException(String msg) {
		super(msg);
	}
}
