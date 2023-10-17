package com.wamk.uber.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.MotoristaNaoEncontradoException;
import com.wamk.uber.exceptions.PassageiroCorrendoException;
import com.wamk.uber.exceptions.PlacaExistenteException;
import com.wamk.uber.exceptions.TelefoneJaExisteException;
import com.wamk.uber.exceptions.UsuarioCorrendoException;
import com.wamk.uber.exceptions.UsuarioDesativadoException;
import com.wamk.uber.exceptions.UsuarioJaAtivoException;
import com.wamk.uber.exceptions.UsuarioJaDesativadoException;
import com.wamk.uber.exceptions.ViagemJaFinalizadaException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		List<Campo> campos = new ArrayList<>();
		
		for(ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError)error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			campos.add(new Campo(nome, mensagem));
		}
		
		ProblemaDeValidacao problema = new ProblemaDeValidacao();
		problema.setTitulo("Um ou mais campos estão inválidos. Por favor preencha-os corretamente!");
		problema.setDataHora(OffsetDateTime.now());
		problema.setStatus(status.value());
		problema.setList(campos);
		
		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Problema> entidadeNaoEncontrada(){
		
		Problema problema = new Problema();
		problema.setTitulo("Id não encontrado.");
		problema.setStatus(HttpStatus.NOT_FOUND.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
	}
	
	@ExceptionHandler(PlacaExistenteException.class)
	public ResponseEntity<Problema> placaExistenteException(){
		
		Problema problema = new Problema();
		problema.setTitulo("Placa já cadastrada.");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(TelefoneJaExisteException.class)
	public ResponseEntity<Problema> telefoneExistenteException(){
		
		Problema problema = new Problema();
		problema.setTitulo("Telefone já casdastrado!");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(PassageiroCorrendoException.class)
	public ResponseEntity<Problema> passageiroCorrendoException(){
		
		Problema problema = new Problema();
		problema.setTitulo("Você precisa esperar que sua corrida acabe antes de solicitar outra viagem!");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(MotoristaNaoEncontradoException.class)
	public ResponseEntity<Problema> motoristaNaoEncontradoException(){
		
		Problema problema = new Problema();
		problema.setTitulo("Desculpe, mas não encotramos nenhum motorista que esteja ativo agora, "
				+ "por favor solicite uma nova viagem depois de um tempo.");
		problema.setStatus(HttpStatus.NOT_FOUND.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
	}
	
	@ExceptionHandler(ViagemJaFinalizadaException.class)
	public ResponseEntity<Problema> viagemJaFinalizadaException(){
		
		Problema problema = new Problema();
		problema.setTitulo("Você não pode terminar uma viagem que já foi finalizada ou cancelada!");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(UsuarioDesativadoException.class)
	public ResponseEntity<Problema> usuarioDesativadoException(){
		
		Problema problema = new Problema();
		problema.setTitulo("O usuario precisa está ativo para realizar essa função.");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(UsuarioJaDesativadoException.class)
	public ResponseEntity<Problema> usuarioJaDesativadoException(){
		
		Problema problema = new Problema();
		problema.setTitulo("O usuario já está desativado");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(UsuarioJaAtivoException.class)
	public ResponseEntity<Problema> usuarioJaAtivoException(){
		
		Problema problema = new Problema();
		problema.setTitulo("O usuario já está ativo");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(UsuarioCorrendoException.class)
	public ResponseEntity<Problema> usuarioCorrendoException(){
		
		Problema problema = new Problema();
		problema.setTitulo("O usuario não pode ser desativado caso esteja em uma corrida.");
		problema.setStatus(HttpStatus.BAD_REQUEST.value());
		problema.setDataHora(OffsetDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
}
