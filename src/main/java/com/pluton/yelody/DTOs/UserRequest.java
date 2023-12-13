package com.pluton.yelody.DTOs;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	
	@Size(min=5 , max=15)
	@NotNull(message="userName must not be NULL")
	private String userName;
	
	@Email
	@NotNull(message="Email must not be NULL")
	private String email;
	
	private String password;
	
	private String description;
	
	private String ageGroup;
	
	private MultipartFile image;
	
}
