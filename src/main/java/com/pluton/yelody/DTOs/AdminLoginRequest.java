package com.pluton.yelody.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequest {
	@NotNull(message = "EMAIL SHOULD NOT BE NULL")
	@Email
	private String email;
	
	@NotNull(message = "PASSWORD SHOULD NOT BE NULL")
	private String password;
}
