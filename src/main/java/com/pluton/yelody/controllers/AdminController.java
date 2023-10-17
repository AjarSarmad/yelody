package com.pluton.yelody.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.models.Admin;
import com.pluton.yelody.models.AdminCreateRequest;
import com.pluton.yelody.models.AdminLoginRequest;
import com.pluton.yelody.services.AdminAuthService;
import com.pluton.yelody.services.AdminService;
import com.pluton.yelody.utilities.HashingUtility;

@RestController
@RequestMapping("/yelody/admin")
public class AdminController {
		@Autowired
		AdminService adminService;
		@Autowired
		HashingUtility hashingUtility;
		@Autowired
		AdminAuthService adminAuthService;
		
		Optional<Admin> adminGet = null;
		Admin adminPost = null;
		
		//CREATE ADMIN
	  	//http://localhost:8080/yelody/admin/postAdmin
		@CrossOrigin(origins = "*")
	    @PostMapping("/postAdmin")
	    public ResponseEntity<Object> postAdmin(@RequestBody AdminCreateRequest adminCreateRequest){
			adminGet = null;
	    	try {
	    		adminGet = adminService.findByEmail(adminCreateRequest.getEmail());
	    		if(adminGet.isEmpty()) {
	    			adminPost = new Admin(
	    					adminCreateRequest.getEmail(),
	    					adminCreateRequest.getUserName(),
	    					hashingUtility.sha256(adminCreateRequest.getPassword()),
	    					adminCreateRequest.getPhone(),
	    					null,
	    					null
	    					);
	    			return adminService.postAdmin(adminPost);
	    		}
		    	else
		    		return ResponseEntity.status(HttpStatus.FOUND).body("EMAIL ALREADY EXISTS");
		    	}
	    	catch(Exception ex) {
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
	    	}
	    }

		//ADMIN LOGIN API
	  	//http://localhost:8080/yelody/admin/loginAdmin
		@CrossOrigin(origins = "*")
	    @PostMapping("/loginAdmin")
	    public ResponseEntity<Object> loginAdmin(@RequestBody AdminLoginRequest adminLoginRequest){
			adminGet = null;
			String pass;
	    	try {
	    		adminGet = adminService.findByEmail(adminLoginRequest.getEmail());
	    		pass = hashingUtility.sha256(adminLoginRequest.getPassword());
	    		
				if(adminGet!=null && pass.equalsIgnoreCase(adminGet.get().getPassword()))
		    		return ResponseEntity.status(HttpStatus.OK).body("LOGIN APPROVED");
		    	else
		    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EMAIL OR PASSWORD IS INCORRECT");
		    	}
	    	catch(Exception ex) {
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
	    	}
	    }
	
		//FORGOT PASSWORD API
  		//http://localhost:8080/yelody/admin/forgotPass?email=
	  	@CrossOrigin(origins = "*")
	    @GetMapping("/forgotPass")
	    public ResponseEntity<Object> forgotPass(@RequestParam (name="email") String email){
			adminGet = null;
	    	try {
    		adminGet = adminService.findByEmail(email);
	    	if(adminGet!=null) {	
	    		//if account exists then email will be sent
	    		adminAuthService.generateOneTimePassword(adminGet.get());
	    		
	    		return ResponseEntity.status(HttpStatus.OK).body("OTP SENT");
	    	}
	    	else
	    		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	    	}
	    	catch(Exception ex) {
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
	    	}
	    }
	    
	  	//OTP VERIFICATION API
  		//http://localhost:8080/yelody/admin/otpVerification?email=&otp
	    @CrossOrigin(origins = "*")
	    @PostMapping("/otpVerification")
	    public ResponseEntity<Object> otpVerification(@RequestParam(name="email") String email , @RequestParam(name="otp") String OTP){
				adminGet = null;
		    	try {
	    		adminGet = adminService.findByEmail(email);
	    		if(adminGet.get().getOtp().equalsIgnoreCase(OTP)) {
		    		return ResponseEntity.status(HttpStatus.OK).body("LOGIN APPROVED");
	    		}
	    		else
		    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("INCORRECT OTP");
	    	}
	    		catch(Exception ex) {
	        		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("INCORRECT EMAIL");
	    		}
	    	
	    }
	
}
