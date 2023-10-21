package com.pluton.yelody.services;
 
import com.pluton.yelody.DTOs.Email;
 
public interface EmailService {
 
    String sendSimpleMail(Email details);
 
}