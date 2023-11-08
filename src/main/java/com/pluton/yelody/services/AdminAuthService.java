package com.pluton.yelody.services;

import com.pluton.yelody.models.Admin;

public interface AdminAuthService {
	public static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes

	public void generateOneTimePassword(Admin admin);
	
	public void clearOTP(Admin admin);
	
	public boolean isOTPRequired(Admin admin);
}
