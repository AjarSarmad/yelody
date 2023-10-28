package com.pluton.yelody.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SuppressWarnings("serial")
public class ConstraintViolationHandler extends RuntimeException {

    public ConstraintViolationHandler(String message) {
		super(message);
	}

	@ExceptionHandler(ConstraintViolationHandler.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationHandler ex) {
        String errorMessage = "Constraint violation: This record is associated with other records and cannot be deleted.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }
}
