package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public Banner saveBanner(Banner banner) {
		try {
			return bannerrepository.save(banner);
		}catch(Exception  e) {
  			return null;
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
		if(banner!=null)
			return banner;
		else
			return null;
	}
}
