package com.pluton.yelody.serviceImpl;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.pluton.yelody.DTOs.FacebookUserDTO;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.AuthService;
import com.pluton.yelody.services.UserService;


@Service
public class AuthServiceImpl implements AuthService{
	
    @Value("${google.client-id}")
    private String clientId;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    
    public ResponseEntity<?> verifyAndFetchUserGoogle(String tokenValue) throws IOException {
        GoogleIdToken idToken;
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

            idToken = verifier.verify(tokenValue);

            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                return userService.findUserByEmail(email)
                        .<ResponseEntity<?>>map(user -> ResponseEntity.ok(user)) 
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("email", email)));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID token.");
            }
        } catch (GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }
    }

    public ResponseEntity<?> verifyAndFetchUserFacebook(String tokenValue) {
        RestTemplate restTemplate = new RestTemplate();
        String facebookApiUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + tokenValue;

        try {
            FacebookUserDTO userInfo = restTemplate.getForObject(facebookApiUrl, FacebookUserDTO.class);

            if (userInfo != null && userInfo.getEmail() != null) {
                return userRepository.findByEmail(userInfo.getEmail())
                        .<ResponseEntity<?>>map(user -> ResponseEntity.ok(user))
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("email", userInfo.getEmail())));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Facebook token or no email found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }
    
}