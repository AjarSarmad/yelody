package com.pluton.yelody.serviceImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.pluton.yelody.models.User;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.GoogleAuthService;
import com.pluton.yelody.services.UserService;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Service
public class GoogleAuthServiceImpl implements GoogleAuthService{
	
    @Value("${google.client-id}")
    private String clientId;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
//    @Autowired
//    WebClient webClient = WebClient.builder().build();
    
    String FACEBOOK_AUTH_URL = "https://graph.facebook.com/me?fields=email,first_name,last_name&access_token=%s";
    
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

//	@Override
//	public ResponseEntity<?> verifyAndFetchUserFacebook(String tokenValue) {
//		String templateUrl = String.format(FACEBOOK_AUTH_URL, tokenValue);
//		@SuppressWarnings("unchecked")
//		Map<String, Object> responseBody = webClient.get()
//                .uri(templateUrl)
//                .retrieve()
//                .onStatus(status -> status.isError(), response -> 
//                    response.bodyToMono(String.class)
//                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(
//                            response.statusCode(), errorBody))))
//                .bodyToMono(Map.class)
//                .block();
//		
//		System.out.print("\n\n\n" + responseBody);
//        final Optional<User> userOptional = userRepository.findByEmail(responseBody.get("email").toString());
//        
//        
//        return null;
//	}
//	private final ObjectMapper objectMapper = new ObjectMapper();
//	
    private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
    public ResponseEntity<?> verifyAndFetchUserFacebook(String tokenValue) {
        String templateUrl = String.format(FACEBOOK_AUTH_URL, tokenValue);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(templateUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    @SuppressWarnings("unchecked")
                    Map<String, Object> responseBody = objectMapper.readValue(content.toString(), Map.class);
                    System.out.println("\n\n\n" + responseBody);

                    if (responseBody.containsKey("email")) {
                        String email = (String) responseBody.get("email");
                        final Optional<User> userOptional = userRepository.findByEmail(email);

                        // Your existing logic to handle the user lookup
                        // ...

                        return ResponseEntity.ok().body(Collections.singletonMap("email", email));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found in the token.");
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to verify token with Facebook.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}