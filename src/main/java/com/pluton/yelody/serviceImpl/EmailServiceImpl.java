package com.pluton.yelody.serviceImpl;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.Email;
import com.pluton.yelody.services.EmailService;
 
@Service
public class EmailServiceImpl implements EmailService {
 
    @Autowired private JavaMailSender javaMailSender;
 
    @Value("${spring.mail.username}") private String sender;
 
    public String sendSimpleMail(Email details)
    {
        try {
 
            SimpleMailMessage mailMessage = new SimpleMailMessage();
 
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getContent());
            mailMessage.setSubject(details.getSubject());
 
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
 
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
}
   