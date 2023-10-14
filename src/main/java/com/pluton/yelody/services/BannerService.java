package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pluton.yelody.models.Banner;

public interface BannerService {
	public abstract ResponseEntity<Object> saveBanner(Banner banner);
	
	public abstract Optional<Banner> getBannerByID(UUID id);
	
	public abstract List<Banner> getBannerList();
	
	public abstract HttpStatus deleteBanner(Banner banner);
	
	public abstract Optional<Banner> getBannerByUrl(String url);
}
