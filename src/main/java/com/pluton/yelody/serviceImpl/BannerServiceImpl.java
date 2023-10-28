package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.Banner;
import com.pluton.yelody.repositories.BannerRepository;
import com.pluton.yelody.services.BannerService;
import com.pluton.yelody.utilities.ImageUtil;

@Service
public class BannerServiceImpl implements BannerService{
	@Autowired
	BannerRepository bannerRepository;
	Optional<Banner> banner = null;
	
	@Override
	public Banner saveBanner(Banner banner) {
		try {
			return bannerRepository.save(banner);
		}catch(Exception  e) {
  			return null;
		}
	}
	
	@Override
	public Optional<Banner> getBannerByID(UUID id){
		return Optional.ofNullable(bannerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BANNER ID: " + id + " NOT FOUND")));
	}
	
	@Override
	public List<Banner> getBannerList(){
		return bannerRepository.findAll();
	}
	
	@Override 
	public HttpStatus deleteBanner(Banner banner) {
		try {
			bannerRepository.delete(banner);
			ImageUtil.deleteFile(banner.getImage());
			return HttpStatus.OK;
		}catch(Exception ex) {
			return HttpStatus.NOT_FOUND;
		}
	}
}
