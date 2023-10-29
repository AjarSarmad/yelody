package com.pluton.yelody.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ValidationExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST) 
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
	    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
	    Map<String, String> errors = new HashMap<>();

	    for (FieldError fieldError : fieldErrors) {
	        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "Validation failed");
	    response.put("errors", errors);

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST) 
	public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
	    String error = "NOT VALID UUID FORMAT";
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "Validation failed");
	    response.put("error", error);

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST) 
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
	    String name = ex.getParameterName();
	    String error = name + " parameter is missing";
	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "Validation failed");
	    response.put("error", error);

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidUUIDFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST) 
    public ResponseEntity<Object> handleInvalidUUIDFormatException(InvalidUUIDFormatException ex) {
		String error = "NOT VALID UUID FORMAT";
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "Validation failed");
	    response.put("error", error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
