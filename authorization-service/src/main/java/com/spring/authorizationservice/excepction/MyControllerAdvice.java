package com.spring.authorizationservice.excepction;


import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
@RestController
public class MyControllerAdvice extends ResponseEntityExceptionHandler{
	
	

	@ExceptionHandler(MalformedJwtException.class)
	public final ResponseEntity<Object> handleException(MalformedJwtException ex, WebRequest request){
		
		ExceptionResponse e=new ExceptionResponse(new Date(),"Session Timeout",request.getDescription(false));
		
		return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
	}
	
	
		
	@ExceptionHandler(InValidCredentials.class)
	public final ResponseEntity<Object> handleException(InValidCredentials ex, WebRequest request){
			
			ExceptionResponse e=new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
			
			return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
		
	}
		
		
	@ExceptionHandler(ExpiredJwtException.class)
	public final ResponseEntity<Object> handleException(ExpiredJwtException ex, WebRequest request){
			
			ExceptionResponse e=new ExceptionResponse(new Date(),"Jwt Token Expired",request.getDescription(false));
			
			return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
		
	}
	

}
