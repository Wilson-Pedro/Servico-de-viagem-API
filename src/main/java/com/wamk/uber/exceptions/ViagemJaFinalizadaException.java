package com.wamk.uber.exceptions;

public class ViagemJaFinalizadaException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ViagemJaFinalizadaException(String msg) {
		super(msg);
	}
}
