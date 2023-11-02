package com.pluton.yelody.services;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

public interface AuthService {
	public abstract ResponseEntity<?> verifyAndFetchUserGoogle(String tokenValue) throws IOException;
	public abstract ResponseEntity<?> verifyAndFetchUserFacebook(String tokenValue);
}
