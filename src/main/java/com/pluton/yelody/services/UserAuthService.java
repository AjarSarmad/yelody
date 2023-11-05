package com.pluton.yelody.services;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.pluton.yelody.DTOs.LoginRequest;
import com.pluton.yelody.models.User;

public interface UserAuthService {
	public static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes
	public abstract ResponseEntity<?> verifyAndFetchUserGoogle(String tokenValue) throws IOException;
	public abstract ResponseEntity<?> verifyAndFetchUserFacebook(String tokenValue);
    public abstract Map<String, Object> getApplePublicKeys();
    public abstract ResponseEntity<?> normalLogin(LoginRequest loginRequest);
    public abstract void generateOneTimePassword(User user);
    public abstract void clearOTP(User user);
    public abstract boolean isOTPRequired(User user);
}
