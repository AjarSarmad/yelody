package com.pluton.yelody.DTOs;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequest {
	
	@NotNull(message="Location must not be NULL")
	private String location;

	@NotNull(message="Language must not be NULL")
	private String language;
	
	@Null
	private MultipartFile image;
	
}