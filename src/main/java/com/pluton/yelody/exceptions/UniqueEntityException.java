package com.pluton.yelody.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class UniqueEntityException extends DataIntegrityViolationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UniqueEntityException(String message) {
        super(message);
    }

    public UniqueEntityException(String message, String duplicateValue) {
        super(message + " (Duplicate value: " + duplicateValue + ")");
    }
}
