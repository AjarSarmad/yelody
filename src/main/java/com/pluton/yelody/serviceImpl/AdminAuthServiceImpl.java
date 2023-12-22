package com.pluton.yelody.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.Email;
import com.pluton.yelody.models.Admin;
import com.pluton.yelody.repositories.AdminRepository;
import com.pluton.yelody.services.AdminAuthService;
import com.pluton.yelody.services.AdminService;
import com.pluton.yelody.services.EmailService;

import net.bytebuddy.utility.RandomString;


@Service
public class AdminAuthServiceImpl implements AdminAuthService{
	
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	AdminService adminService;
	
	@Override
	public void generateOneTimePassword(Admin admin) {
		String OTP;
		if(isOTPRequired(admin)) {
		    OTP = admin.getOtp();
		}
		else {
		clearOTP(admin);
	    OTP = RandomString.make(5).toUpperCase();
	    admin.setOtp(OTP);
	    admin.setOtpRequestedTime(new Date());
	    adminService.updateAdmin(admin);
	}
		
	    String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
	    String content = "Hello " + admin.getUserName() + ",\n"
	            + "For security reason, you're required to use the following \n"
	            + "One Time Password to login:\n"
	            + "\n\t\t\t\t" + OTP + "\n"
	            + "\nNote: this OTP is set to expire in 5 minutes.";
	    
	    	    
	    //Sending EMAIL
	    Email email = new Email(admin.getEmail(), content, subject);
	    emailService.sendSimpleMail(email);
		
	}

	@Override
	public void clearOTP(Admin admin) {
		admin.setOtp(null);
		admin.setOtpRequestedTime(null);
		adminService.updateAdmin(admin);		
	}

	@Override
	public boolean isOTPRequired(Admin admin) {
	       if (admin.getOtp() == null) {
	            return false;
	        }
	         
	        long currentTimeInMillis = System.currentTimeMillis();
	        long otpRequestedTimeInMillis = admin.getOtpRequestedTime().getTime();
	         
	        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
	            // OTP expires
	            return false;
	        }
	         
	        return true;
	}

}
