package com.pluton.yelody.utilities;

import java.security.MessageDigest;

import org.springframework.stereotype.Service;

@Service
public class HashingUtility {
	
		public String sha256(final String base) {
		    try{
		        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
		        final byte[] hash = digest.digest(base.getBytes("UTF-8"));
		        final StringBuilder hexString = new StringBuilder();
		        for (int i = 0; i < hash.length; i++) {
		            final String hex = Integer.toHexString(0xff & hash[i]);
		            if(hex.length() == 1) 
		              hexString.append('0');
		            hexString.append(hex);
		        }
		        return hexString.toString();
		    } catch(Exception ex){
		       throw new RuntimeException(ex);
		    }
		}

	
}