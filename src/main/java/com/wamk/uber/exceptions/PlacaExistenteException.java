package com.wamk.uber.exceptions;

public class PlacaExistenteException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PlacaExistenteException(String msg) {
		super(msg);
	}
}
