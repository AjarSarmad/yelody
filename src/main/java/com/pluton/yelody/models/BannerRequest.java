package com.pluton.yelody.models;

import org.hibernate.validator.constraints.URL;

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
	
}