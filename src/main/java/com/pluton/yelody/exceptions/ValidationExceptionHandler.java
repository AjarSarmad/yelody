package com.pluton.yelody.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.pluton.yelody.exceptions.SongRequestExceptions.AgeGroupNotFoundException;
import com.pluton.yelody.exceptions.SongRequestExceptions.ChartNotFoundException;
import com.pluton.yelody.exceptions.SongRequestExceptions.GenreNotFoundException;
import com.pluton.yelody.exceptions.SongRequestExceptions.KeywordNotFoundException;

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
	
	@ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<Object> handleGenreNotFoundException(GenreNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KeywordNotFoundException.class)
    public ResponseEntity<Object> handleKeywordNotFoundException(KeywordNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChartNotFoundException.class)
    public ResponseEntity<Object> handleChartNotFoundException(ChartNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgeGroupNotFoundException.class)
    public ResponseEntity<Object> handleAgeGroupNotFoundException(AgeGroupNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
}
