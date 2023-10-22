package com.pluton.yelody.exceptions;

@SuppressWarnings("serial")
public class SongRequestExceptions extends RuntimeException {
	public SongRequestExceptions(String message) {
        super(message);
    }
}