package com.pluton.yelody.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordRequest {
	@NotNull
    @NotBlank(message = "EMAIL SHOULD NOT BE BLANK")
    @Email
    private String email;
    
    @NotNull
    @NotBlank(message = "OTP SHOULD NOT BE BLANK")
    private String otp;
    
    @NotNull
    @NotBlank(message = "NEW PASSWORD SHOULD NOT BE BLANK")
    private String newPassword;
}
