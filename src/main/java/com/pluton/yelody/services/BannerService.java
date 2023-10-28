package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.pluton.yelody.models.Banner;

public interface BannerService {
	public abstract Banner saveBanner(Banner banner);
	
	public abstract Optional<Banner> getBannerByID(UUID id);
	
	public abstract List<Banner> getBannerList();
	
	public abstract HttpStatus deleteBanner(Banner banner);
}
