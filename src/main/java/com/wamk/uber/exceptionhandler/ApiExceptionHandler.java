package com.wamk.uber.exceptionhandler;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Problema> entidadeNaoEncontrada(){
		
		Problema problema = new Problema();
		problema.setTitulo("Entidade não Encontrada.");
		problema.setStatus(HttpStatus.NOT_FOUND.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
	}
}
