package com.pluton.yelody.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.BannerRequest;
import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.Banner;
import com.pluton.yelody.services.BannerService;
import com.pluton.yelody.utilities.ImageUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/banner")
public class BannerController {
	@Autowired
	BannerService bannerService;
	
	final String imagePath = "Resources/IMAGE/BANNER";
	Optional<Banner> bannerGet = null;
	Banner bannerPost = null;
	List<Banner> bannerList = null;
	
	//Create Banner
	//http://localhost:8080/yelody/banner/addBanner
	@CrossOrigin(origins = "*")
	@PostMapping("/addBanner")
	public GenericResponse<Banner> addBanner(@ModelAttribute @Valid BannerRequest bannerRequest) throws IOException{
		bannerPost = null;
		String imageResponse = null;
		try {
			UUID id = UUID.randomUUID();
			if(bannerRequest.getImage()!=null  && !bannerRequest.getImage().isEmpty())
				imageResponse = ImageUtil.saveFile(imagePath, id.toString(), bannerRequest.getImage());
			else
				return GenericResponse.error("IMAGE CANNOT BE NULL");
			
	        bannerPost = new Banner(
	        		id,
	            bannerRequest.getLocation(),
	            bannerRequest.getLanguage(),
	            bannerRequest.getUrl(),
	            imageResponse
	        );
			
			Banner response = bannerService.saveBanner(bannerPost);
			if(response!=null ) {
				return GenericResponse.success(response, "BANNER CREATED SUCCESSFULLY");
			}else{
				return GenericResponse.error("BANNER ALREADY EXISTS");
			}
		}catch(Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}
	}
    
    //GET BANNER DETAILS BY ID
  	//http://localhost:8080/yelody/banner/getBannerById?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getBannerById")
    public GenericResponse<Banner> getBannerById(@RequestParam(name="id") UUID id){
    	bannerGet = null;
    	try {
    		bannerGet = bannerService.getBannerByID(id);
    		if(bannerGet!=null)
    			return GenericResponse.success(bannerGet.get());
    		else
    			return GenericResponse.error("BANNER NOT FOUND");

    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    	
    //GET LIST OF BANNERS
  	//http://localhost:8080/yelody/banner/getBannerList
    @CrossOrigin(origins = "*")
  	@GetMapping("/getBannerList")
    public GenericResponse<List<Banner>> getBannerList(){
    	bannerList = null;
    	try {
    		bannerList = new ArrayList<>(bannerService.getBannerList());
    		if(bannerList!=null)
    			return GenericResponse.success(bannerList);
    		else
    			return GenericResponse.error("BANNER NOT FOUND");

    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
    //UPDATE BANNER DETAILS
  	//http://localhost:8080/yelody/banner/updateBanner?id=
    @CrossOrigin(origins = "*")
  	@PutMapping("/updateBanner")
    public GenericResponse<Banner> updateBanner(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id, @ModelAttribute @Valid BannerRequest bannerViewModel){
    	bannerGet = null;
    	bannerPost = null;
    	String imageResponse = null;
    	try {
    		bannerGet = bannerService.getBannerByID(id);
			if(bannerViewModel.getImage()!=null  && !bannerViewModel.getImage().isEmpty())
				imageResponse = ImageUtil.saveFile(imagePath, bannerGet.get().getBannerId().toString(), bannerViewModel.getImage());
    		
    		if(bannerGet!=null) {
    	  		bannerPost = new Banner(
        				bannerGet.get().getBannerId(),
    					bannerViewModel.getLocation(),
    					bannerViewModel.getLanguage(),
    					bannerViewModel.getUrl(),
    					bannerViewModel.getImage()==null?bannerGet.get().getImage():imageResponse
    					);
    	  		return GenericResponse.success(bannerService.saveBanner(bannerPost));
    			}
    		else
    			return GenericResponse.error("BANNER NOT FOUND");

    	}catch(Exception ex) {
			ex.printStackTrace();
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
    //DELETE BANNER DETAILS
  	//http://localhost:8080/yelody/banner/deleteBanner?id=
    @CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteBanner")
    public GenericResponse deleteBanner(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) throws EntityNotFoundException {
    	bannerGet = null;
    	try {
        	bannerGet = bannerService.getBannerByID(id);
    		if(bannerGet!=null) {
    			bannerService.deleteBanner(bannerGet.get());
    			return GenericResponse.success("Banner ID:" + id + " Deleted Successfully");
    		}
    		else
    			return GenericResponse.error("Banner ID:" + id + " NOT FOUND");

    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
}
