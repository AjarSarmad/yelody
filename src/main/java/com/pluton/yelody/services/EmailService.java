package com.pluton.yelody.services;
 
import com.pluton.yelody.models.Email;
 
public interface EmailService {
 
    String sendSimpleMail(Email details);
 
}