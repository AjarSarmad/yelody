package com.pluton.yelody.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.services.GoogleAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/login")
public class AuthController {
	@Autowired
	GoogleAuthService googleService;
	
    //http://localhost:8080/yelody/login/google
	@CrossOrigin(origins = "*")
	@PostMapping("/google")
    public ResponseEntity<?> handleToken(@RequestParam(name = "token") String tokenValue) {
		try {
	        return googleService.verifyAndFetchUserGoogle(tokenValue);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
    }
	
    //http://localhost:8080/yelody/login/facebook
	@CrossOrigin(origins = "*")
	@PostMapping("facebook")
    public ResponseEntity<?> facebook(@RequestParam(name = "token") String tokenValue) {
        try {
            return googleService.verifyAndFetchUserFacebook(tokenValue);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
    }
}
