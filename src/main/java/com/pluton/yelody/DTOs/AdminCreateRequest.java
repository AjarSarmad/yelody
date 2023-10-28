package com.pluton.yelody.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateRequest {
	@NotNull(message = "EMAIL SHOULD NOT BE NULL")
	@Email
	private String email;
	
	@NotNull(message = "USERNAME SHOULD NOT BE NULL")
	private String userName;
	
	@NotNull(message = "PASSWORD SHOULD NOT BE NULL")
	private String password;
	
	@NotNull(message = "PHONE SHOULD NOT BE NULL")
	private String phone;

}
