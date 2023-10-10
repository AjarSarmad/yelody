package com.pluton.yelody.controllers;

import java.io.IOException;
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
import com.pluton.yelody.models.BannerViewModel;
import com.pluton.yelody.repositories.BannerRepository;
import com.pluton.yelody.serviceImpl.ImageUtil;

@RestController
@RequestMapping("/yelody/banner")
public class BannerController {
	@Autowired
	BannerRepository bannerRepository;
	
	//Create Banner
	//http://localhost:8080/yelody/banner/addBanner
	@CrossOrigin(origins = "*")
	@PostMapping("addBanner")
	public ResponseEntity<Object> addBanner(@RequestBody BannerViewModel bannerViewModel){
		try {
			return new ResponseEntity<Object>(bannerRepository.save(
						new Banner(
								UUID.randomUUID(),
								bannerViewModel.getLocation(),
								bannerViewModel.getUrl(),
								bannerViewModel.getLanguage(),
								null
								)),
					HttpStatus.CREATED);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
		}
	}
	
	//ADD BANNER IMAGE
  	//http://localhost:8080/yelody/banner/addBannerImage?id=
    @CrossOrigin(origins = "*")
  	@PostMapping("/addBannerImage")
  	public ResponseEntity<Object> addImage(@RequestParam(name="id") UUID id , @RequestPart("file") MultipartFile file) throws IOException{
  		
    	Optional<Banner> banner = bannerRepository.findById(id);
    	if(banner!=null){
    		banner.get().setBannerImage(ImageUtil.compressImage(file.getBytes()));
  			return new ResponseEntity<Object>(bannerRepository.save(banner.get()),HttpStatus.CREATED);
  		}
  		else
  			return new ResponseEntity<Object>(HttpStatus.FOUND);
  	}
    
    //GET BANNER DETAILS bY ID
  	//http://localhost:8080/yelody/banner/getBannerById?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getBannerById")
    public ResponseEntity<Object> getBannerById(@RequestParam(name="id") UUID id){
    	try {
    		Optional<Banner> banner = bannerRepository.findById(id);
    		if(banner!=null)
    			return new ResponseEntity<Object>(banner, HttpStatus.OK);
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
    	try {
    		List<Banner> bannerList = bannerRepository.findAll();
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
    public ResponseEntity<Object> updateBanner(@RequestParam(name="id") UUID id, @RequestBody BannerViewModel bannerViewModel){
    	try {
    		Optional<Banner> banner = bannerRepository.findById(id);
    		if(banner!=null) {
    			return new ResponseEntity<Object>(bannerRepository.save(new Banner(
    					banner.get().getBannerId(),
    					bannerViewModel.getLocation(),
    					bannerViewModel.getUrl(),
    					bannerViewModel.getLanguage(),
    					banner.get().getBannerImage()
    					)), HttpStatus.OK);
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
    public ResponseEntity<Object> getImage(@RequestParam(name="id") UUID id){
  		
    	try {
    	Optional<Banner> banner = bannerRepository.findById(id);
    	if(banner!=null) {
    		byte[] image = ImageUtil.decompressImage(banner.get().getBannerImage());
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
    public ResponseEntity<Object> deleteBanner(@RequestParam(name="id") UUID id){
    	try {
    		Optional<Banner> banner = bannerRepository.findById(id);
    		if(banner!=null) {
    			bannerRepository.delete(banner.get());
    			return new ResponseEntity<Object>(HttpStatus.OK);
    		}
    		else
    			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
    
}
