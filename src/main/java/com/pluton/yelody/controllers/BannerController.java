package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.pluton.yelody.models.Banner;
import com.pluton.yelody.services.BannerService;
import com.pluton.yelody.utilities.ImageUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/banner")
public class BannerController {
	@Autowired
	BannerService bannerService;
	
	final String imagePath = "ImageResources/BANNER";
	Optional<Banner> bannerGet = null;
	Banner bannerPost = null;
	List<Banner> bannerList = null;
	
	//Create Banner
	//http://localhost:8080/yelody/banner/addBanner
	@CrossOrigin(origins = "*")
	@PostMapping("/addBanner")
	public ResponseEntity<Object> addBanner(@ModelAttribute @Valid BannerRequest bannerRequest){
		bannerPost = null;
		String imageResponse = null;
		try {
			UUID id = UUID.randomUUID();
//			if(bannerRequest.getImage()!=null) {
			if(!bannerRequest.getImage().isEmpty())
				imageResponse = ImageUtil.saveFile(imagePath, id.toString(), bannerRequest.getImage());
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IMAGE CANNOT BE NULL");
//			}
			
	        bannerPost = new Banner(
	        		id,
	            bannerRequest.getLocation(),
	            bannerRequest.getLanguage(),
	            imageResponse
	        );
			
			Banner response = bannerService.saveBanner(bannerPost);
			if(response!=null ) {
				return new ResponseEntity<Object>(response, HttpStatus.CREATED);
			}else{
				return ResponseEntity.status(HttpStatus.FOUND).body("BANNER ALREADY EXISTS");
			}
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
	}
    
    //GET BANNER DETAILS BY ID
  	//http://localhost:8080/yelody/banner/getBannerById?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getBannerById")
    public ResponseEntity<Object> getBannerById(@RequestParam(name="id") UUID id){
    	bannerGet = null;
    	try {
    		bannerGet = bannerService.getBannerByID(id);
    		if(bannerGet!=null)
    			return new ResponseEntity<Object>(bannerGet.get(), HttpStatus.OK);
    		else
      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
    	
    //GET LIST OF BANNERS
  	//http://localhost:8080/yelody/banner/getBannerList
    @CrossOrigin(origins = "*")
  	@GetMapping("/getBannerList")
    public ResponseEntity<Object> getBannerList(){
    	bannerList = null;
    	try {
    		bannerList = new ArrayList<>(bannerService.getBannerList());
    		if(bannerList!=null)
    			return new ResponseEntity<Object>(bannerList, HttpStatus.OK);
    		else
      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    //UPDATE BANNER DETAILS
  	//http://localhost:8080/yelody/banner/updateBanner?id=
    @CrossOrigin(origins = "*")
  	@PutMapping("/updateBanner")
    public ResponseEntity<Object> updateBanner(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id, @ModelAttribute @Valid BannerRequest bannerViewModel){
    	bannerGet = null;
    	bannerPost = null;
    	String imageResponse = null;
    	try {
    		bannerGet = bannerService.getBannerByID(id);
    		if(!bannerViewModel.getImage().isEmpty())
				imageResponse = ImageUtil.saveFile(imagePath, bannerGet.get().getBannerId().toString(), bannerViewModel.getImage());
    		
    		if(bannerGet!=null) {
    	  		bannerPost = new Banner(
        				bannerGet.get().getBannerId(),
    					bannerViewModel.getLocation(),
    					bannerViewModel.getLanguage(),
    					bannerViewModel.getImage().isEmpty()?bannerGet.get().getImage():imageResponse
    					);
    			return new ResponseEntity<Object>(bannerService.saveBanner(bannerPost), HttpStatus.OK);
    			}
    		else
    			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BANNER NOT FOUND");

    	}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    	}
    }
    
    //DELETE BANNER DETAILS
  	//http://localhost:8080/yelody/banner/deleteBanner?id=
    @CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteBanner")
    public ResponseEntity<Object> deleteBanner(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
    	bannerGet = null;
    	try {
        	bannerGet = bannerService.getBannerByID(id);
    		if(bannerGet!=null) {
    			bannerService.deleteBanner(bannerGet.get());
    			return new ResponseEntity<Object>(HttpStatus.OK);
    		}
    		else
    			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
    
}
