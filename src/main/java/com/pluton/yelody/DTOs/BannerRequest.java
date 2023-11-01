package com.pluton.yelody.DTOs;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
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
	
	@NotNull(message = "URL must not be NULL")
	@URL
	private String url;
	
	private MultipartFile image;
	
}