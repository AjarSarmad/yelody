package com.pluton.yelody.exceptions;

@SuppressWarnings("serial")
public class InvalidUUIDFormatException extends RuntimeException {
    public InvalidUUIDFormatException(String message) {
        super(message);
    }
}
