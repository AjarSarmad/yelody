package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluton.yelody.models.Banner;

public interface BannerRepository extends JpaRepository<Banner , UUID>{
	Optional<Banner> findBannerByUrl(String url); 
}
