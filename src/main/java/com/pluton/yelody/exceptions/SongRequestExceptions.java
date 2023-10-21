package com.pluton.yelody.exceptions;

public class SongRequestExceptions {
	
	public static class KeywordNotFoundException extends RuntimeException {
	    public KeywordNotFoundException(String message) {
	        super(message);
	    }
	}

	public static class AgeGroupNotFoundException extends RuntimeException {
	    public AgeGroupNotFoundException(String message) {
	        super(message);
	    }
	}

	public static class ChartNotFoundException extends RuntimeException {
	    public ChartNotFoundException(String message) {
	        super(message);
	    }
	}

	public static class GenreNotFoundException extends RuntimeException {
	    public GenreNotFoundException(String message) {
	        super(message);
	    }
	}

}
