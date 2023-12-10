package com.pluton.yelody.serviceImpl;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.pluton.yelody.DTOs.Email;
import com.pluton.yelody.DTOs.FacebookUserDTO;
import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.LoginRequest;
import com.pluton.yelody.models.User;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.EmailService;
import com.pluton.yelody.services.UserAuthService;
import com.pluton.yelody.services.UserService;
import com.pluton.yelody.utilities.HashingUtility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.bytebuddy.utility.RandomString;


@Service
public class UserAuthServiceImpl implements UserAuthService{
	
    @Value("${google.client-id}")
    private String clientId;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HashingUtility hashingUtility;
    @Autowired
	EmailService emailService;
    
    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";
    
    public GenericResponse verifyAndFetchUserGoogle(String tokenValue) throws IOException {
    	RestTemplate restTemplate = new RestTemplate();
    	String googleApiUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + tokenValue;
        try {
        	@SuppressWarnings("unchecked")
			Map<String, String> response = restTemplate.getForObject(googleApiUrl, Map.class);
        	
            if (response != null) {
                String email = response.get("email");
                return userService.findUserByEmail(email)
                        .<GenericResponse>map(user -> GenericResponse.success(user)) 
                        .orElseGet(() -> GenericResponse.success(Collections.singletonMap("email", email), "NEW USER"));
            } else {
            	return GenericResponse.error("Invalid ID token.");
            }
        } catch (Exception e) {
        	return GenericResponse.error("Invalid ID token.");
        }
    }

    public GenericResponse verifyAndFetchUserFacebook(String tokenValue) {
        RestTemplate restTemplate = new RestTemplate();
        String facebookApiUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + tokenValue;

        try {
            FacebookUserDTO userInfo = restTemplate.getForObject(facebookApiUrl, FacebookUserDTO.class);

            if (userInfo != null && userInfo.getEmail() != null) {
                return userRepository.findByEmail(userInfo.getEmail())
                        .<GenericResponse>map(user -> GenericResponse.success(user))
                        .orElseGet(() -> GenericResponse.success(Collections.singletonMap("email", userInfo.getEmail()), "NEW USER"));
            } else {
            	return GenericResponse.error("Invalid Facebook token or no email found.");
            }
        } catch (Exception e) {
        	return GenericResponse.error(e.getLocalizedMessage());
        }
    }
    
    public Map<String, Object> getApplePublicKeys() {
        RestTemplate restTemplate = new RestTemplate();
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(APPLE_PUBLIC_KEYS_URL, Map.class);
        return response;
    }
    
    
    public Jws<Claims> decodeAppleJwt(String jwtToken) {
    	 // Fetch Apple's public keys
        Map<String, Object> appleKeys = getApplePublicKeys();
        List<Map<String, String>> keys = (List<Map<String, String>>) appleKeys.get("keys");

        // Parse the JWT token header to extract the 'kid'
        JwsHeader<?> header = Jwts.parserBuilder().build().parseClaimsJws(jwtToken).getHeader();
        String kid = header.getKeyId();

        // Find the key with a matching 'kid'
        PublicKey publicKey = null;
        for (Map<String, String> key : keys) {
            if (kid != null && kid.equals(key.get("kid"))) {
                String n = key.get("n");
                String e = key.get("e");
                publicKey = toPublicKey(n, e);
                break;
            }
        }

        if (publicKey == null) {
            throw new JwtException("Failed to find public key to verify JWT");
        }

        // Now parse and validate JWT with the public key
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwtToken);
    }

    private PublicKey toPublicKey(String n, String e) {
        byte[] nBytes = Base64.getUrlDecoder().decode(n);
        byte[] eBytes = Base64.getUrlDecoder().decode(e);

        RSAPublicKey publicKey = (RSAPublicKey) Keys.keyPairFor(SignatureAlgorithm.RS256).getPublic();
        return (PublicKey) Keys.hmacShaKeyFor(nBytes);
    }

	@Override
	public ResponseEntity<?> normalLogin(LoginRequest loginRequest) {
		Optional<User> userGet = null;
		String pass;
    	try {
    		userGet = userService.findUserByEmail(loginRequest.getEmail());
    		pass = hashingUtility.sha256(loginRequest.getPassword());
    		
			if(userGet!=null && pass.equalsIgnoreCase(userGet.get().getPassword()))
	    		return ResponseEntity.status(HttpStatus.OK).body("LOGIN APPROVED");
	    	else
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EMAIL OR PASSWORD IS INCORRECT");
	    	}
    	catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EMAIL OR PASSWORD IS INCORRECT");
    	}
	}
	
		
		
	@Override
	public void generateOneTimePassword(User user) {
		String OTP;
		if(isOTPRequired(user)) {
		    OTP = user.getOtp();
		}
		else {
		clearOTP(user);
	    OTP = RandomString.make(5).toUpperCase();
	    user.setOtp(OTP);
	    user.setOtpRequestedTime(new Date(System.currentTimeMillis()));
	    userService.updateUser(user);
	}
		
	    String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
	    String content = "Hello " + user.getUserName() + ",\n"
	            + "For security reason, you're required to use the following \n"
	            + "One Time Password to login:\n"
	            + "\n\t\t\t\t" + OTP + "\n"
	            + "\nNote: this OTP is set to expire in 5 minutes.";
	    
	    	    
	    //Sending EMAIL
	    Email email = new Email(user.getEmail(), content, subject);
	    emailService.sendSimpleMail(email);
		
	}

	@Override
	public void clearOTP(User user) {
		user.setOtp(null);
		user.setOtpRequestedTime(null);
		userService.updateUser(user);		
	}

	@Override
	public boolean isOTPRequired(User user) {
	       if (user.getOtp() == null) {
	            return false;
	        }
	         
	        long currentTimeInMillis = System.currentTimeMillis();
	        long otpRequestedTimeInMillis = user.getOtpRequestedTime().getTime();
	         
	        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
	            // OTP expires
	            return false;
	        }
	        return true;
	}

}