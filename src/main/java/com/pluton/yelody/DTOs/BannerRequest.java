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

	@URL
	private String url;
	
	@NotNull(message="Language must not be NULL")
	private String language;
	
	private MultipartFile image;
	
}