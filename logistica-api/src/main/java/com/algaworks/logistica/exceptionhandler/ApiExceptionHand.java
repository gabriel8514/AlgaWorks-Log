package com.algaworks.logistica.exceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.logistica.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHand extends ResponseEntityExceptionHandler{

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Problema.Campo> campos = new ArrayList<>(); 		
		
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
		
			String nome = ((FieldError) error).getField();
			String mensagem = error.getDefaultMessage(); 
			
			campos.add(new Problema.Campo(nome, mensagem)); 
		}
		
		Problema bo = new Problema(); 
		bo.setStatus(status.value());
		bo.setDataHora(LocalDateTime.now());
		bo.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
		bo.setCampos(campos);
		
		return handleExceptionInternal(ex, bo, headers, status, request); 
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST; 
		
		Problema bo = new Problema(); 
		bo.setStatus(status.value());
		bo.setDataHora(LocalDateTime.now());
		bo.setTitulo(ex.getMessage());
		
		return handleExceptionInternal(ex, bo, new HttpHeaders(), status, request); 
	}
}
