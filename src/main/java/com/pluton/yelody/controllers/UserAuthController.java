package com.pluton.yelody.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.ChangePasswordRequest;
import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.LoginRequest;
import com.pluton.yelody.models.User;
import com.pluton.yelody.services.UserAuthService;
import com.pluton.yelody.services.UserService;
import com.pluton.yelody.utilities.HashingUtility;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/login")
public class UserAuthController {
	@Autowired
	UserAuthService authService;
	@Autowired
	UserService userService;
	@Autowired
	HashingUtility hashingUtility;
	
    //http://localhost:8080/yelody/login/google
	@CrossOrigin(origins = "*")
	@PostMapping("/google")
    public GenericResponse handleToken(@RequestParam(name = "token") String tokenValue) {
		try {
	        return authService.verifyAndFetchUserGoogle(tokenValue);
		}catch(Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}
    }
	
    //http://localhost:8080/yelody/login/facebook
	@CrossOrigin(origins = "*")
	@PostMapping("/facebook")
    public GenericResponse facebook(@RequestParam(name = "token") String tokenValue) {
        try {
        	return authService.verifyAndFetchUserFacebook(tokenValue);
		}catch(Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}
    }
	
	//http://localhost:8080/yelody/login/normalLogin
	@CrossOrigin(origins = "*")
	@PostMapping("/normalLogin")
    public ResponseEntity<?> normalLogin(@RequestBody @Valid LoginRequest loginRequest) {
        try {
        	return authService.normalLogin(loginRequest);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
    }
	
	//FORGOT PASSWORD API
	//http://localhost:8080/yelody/login/forgotPass?email=
  	@CrossOrigin(origins = "*")
  	@PostMapping("/forgotPass")
    public ResponseEntity<Object> forgotPass(@RequestParam (name="email") String email){
		Optional<User> userGet = null;
    	try {
    		userGet = userService.findUserByEmail(email);
    	if(userGet!=null) {	
    		//if account exists then email will be sent
    		authService.generateOneTimePassword(userGet.get());
    		
    		return ResponseEntity.status(HttpStatus.OK).body("OTP SENT");
    	}
    	else
    		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    	catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("INCORRECT EMAIL");
    	}
    }
    
	 // CHANGE PASSWORD
	 // http://localhost:8080/yelody/login/changePassword
	 @CrossOrigin(origins = "*")
	 @PostMapping("/changePassword")
	 public ResponseEntity<Object> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
		Optional<User> userGet = null;
	     try {
	    	 userGet = userService.findUserByEmail(changePasswordRequest.getEmail());

	         if (userGet.isPresent()) {
	             User user = userGet.get();
	             String storedOtp = user.getOtp();

	             if (storedOtp != null && storedOtp.equalsIgnoreCase(changePasswordRequest.getOtp())) {
	            	 user.setPassword(hashingUtility.sha256(changePasswordRequest.getNewPassword()));
	                 userService.updateUser(user);
	                 return ResponseEntity.status(HttpStatus.OK).body("PASSWORD UPDATED SUCCESSFULLY");
	             } else {
	                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID OTP OR EMAIL");
	             }
	         } else {
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EMAIL NOT FOUND");
	         }

	     } catch (Exception ex) {
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
	     }
	 }
}
