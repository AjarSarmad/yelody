package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.models.Banner;
import com.pluton.yelody.repositories.BannerRepository;
import com.pluton.yelody.services.BannerService;

@Service
public class BannerServiceImpl implements BannerService{
	@Autowired
	BannerRepository bannerrepository;
	Optional<Banner> banner = null;
	
	@Override
	public ResponseEntity<Object> saveBanner(Banner banner) {
		try {
			return new ResponseEntity<Object>(bannerrepository.save(banner),HttpStatus.CREATED);
		}catch(Exception  e) {
  			return new ResponseEntity<Object>(HttpStatus.FOUND);
		}
	}
	
	@Override
	public Optional<Banner> getBannerByID(UUID id){
		return bannerrepository.findById(id);
	}
	
	@Override
	public List<Banner> getBannerList(){
		return bannerrepository.findAll();
	}
	
	@Override 
	public HttpStatus deleteBanner(Banner banner) {
		bannerrepository.delete(banner);
		return HttpStatus.OK;
	}
	
	@Override
	public Optional<Banner> getBannerByUrl(String url){
		banner = bannerrepository.findBannerByUrl(url);
		if(banner.get()!=null)
			return banner;
		else
			return null;
	}
}
