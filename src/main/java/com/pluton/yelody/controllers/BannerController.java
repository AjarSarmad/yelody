package com.pluton.yelody.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pluton.yelody.models.Banner;
import com.pluton.yelody.models.BannerRequest;
import com.pluton.yelody.services.BannerService;
import com.pluton.yelody.utilities.ImageUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/banner")
public class BannerController {
	@Autowired
	BannerService bannerService;
	
	Optional<Banner> bannerGet = null;
	Banner bannerPost = null;
	List<Banner> bannerList = null;
	
	//Create Banner
	//http://localhost:8080/yelody/banner/addBanner
	@CrossOrigin(origins = "*")
	@PostMapping("/addBanner")
	public ResponseEntity<Object> addBanner(@RequestBody @Valid BannerRequest bannerViewModel){
		bannerPost = null;
		try {
			bannerPost = new Banner(
					UUID.randomUUID(),
					bannerViewModel.getLocation(),
					bannerViewModel.getUrl(),
					bannerViewModel.getLanguage(),
					null
					);
			
			return bannerService.saveBanner(bannerPost);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
		}
	}
	
	//ADD BANNER IMAGE
  	//http://localhost:8080/yelody/banner/addBannerImage?id=
    @CrossOrigin(origins = "*")
  	@PostMapping("/addBannerImage")
  	public ResponseEntity<Object> addImage(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id , @RequestPart("file") MultipartFile file) throws IOException{
  		bannerGet=null;
    	try {
  		bannerGet = bannerService.getBannerByID(id);
    	if(bannerGet!=null){
    		bannerGet.get().setBannerImage(ImageUtil.compressImage(file.getBytes()));
  			return new ResponseEntity<Object>(bannerService.saveBanner(bannerGet.get()),HttpStatus.CREATED);
  		}
  		else
  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    	}
    	catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
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
    public ResponseEntity<Object> updateBanner(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id, @RequestBody @Valid BannerRequest bannerViewModel){
    	bannerGet = null;
    	bannerPost = null;
    	try {
    		bannerGet = bannerService.getBannerByID(id);
    		bannerPost = new Banner(
    				bannerGet.get().getBannerId(),
					bannerViewModel.getLocation(),
					bannerViewModel.getUrl(),
					bannerViewModel.getLanguage(),
					bannerGet.get().getBannerImage()
					);
    		if(bannerGet!=null) {
    			return new ResponseEntity<Object>(bannerService.saveBanner(bannerPost), HttpStatus.OK);
    			}
    		else
    			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    	}
    }
    
    
    //GET BANNER IMAGE
  	//http://localhost:8080/yelody/banner/getBannerImage?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getBannerImage")
    public ResponseEntity<Object> getImage(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
  		bannerGet = null;
    	try {
    	bannerGet = bannerService.getBannerByID(id);
    	if(bannerGet!=null) {
    		byte[] image = ImageUtil.decompressImage(bannerGet.get().getBannerImage());
    		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(image);
    	}else
  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
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
