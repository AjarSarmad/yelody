package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.services.KeywordService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/yelody/keyword")
public class KeywordController {
	@Autowired
	KeywordService keywordService;
	
	Optional<Keyword> keywordGet = null;
	Keyword keywordPost = null;
	List<Keyword> keywordList = null;
	
	//Create a Keyword
	//http://localhost:8080/yelody/keyword/addKeyword
	@CrossOrigin(origins = "*")
	@PostMapping("/addKeyword")
	public GenericResponse<Keyword> addKeyword(@RequestParam(name="keyword")
	@NotNull(message = "Keyword must not be null") 
	@NotBlank
	String keyword){
		keywordPost = null;
		if(keyword.isEmpty())
			return GenericResponse.error("KEYWORD MUST NOT BE EMPTY");
		try {
				keywordPost = new Keyword(
	            UUID.randomUUID(),
	            keyword,
	            new ArrayList<Song>(),
	            null
	            ); 
			
			return keywordService.saveKeyword(keywordPost);
		}catch(Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}
	}
	
	//GET KEYWORD DETAILS BY ID
	//http://localhost:8080/yelody/keyword/getKeywordById?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getKeywordById")
    public GenericResponse<Keyword> getKeywordById(@RequestParam(name="id")@org.hibernate.validator.constraints.UUID UUID id){
    	keywordGet = null;
    	try {
    		keywordGet = keywordService.getKeywordById(id);
    		if(keywordGet!=null)
    			return GenericResponse.success(keywordGet.get());
    		else
    			return GenericResponse.error("NOT FOUND");

    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
    
      //GET LIST OF KEYWORDs
	  //http://localhost:8080/yelody/keyword/getKeywordList
      @CrossOrigin(origins = "*")
	  @GetMapping("/getKeywordList")
	  public GenericResponse<List<Keyword>> getKeywordList(){
    	  keywordList = null;
	  	try {
	  		keywordList = new ArrayList<>(keywordService.getKeywordList());
	  		if(keywordList!=null)
	  			return GenericResponse.success(keywordList);
	  		else
    			return GenericResponse.error("NOT FOUND");
	
	  	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
	  	}
	  }
      
      
      //UPDATE KEYWORD DETAILS
	  //http://localhost:8080/yelody/keyword/updateKeyword?id=&keyword=
      @CrossOrigin(origins = "*")
      @PutMapping("/updateKeyword")
      public GenericResponse<Keyword> updateKeyword(@RequestParam(name="id")
      @org.hibernate.validator.constraints.UUID UUID id,
      @RequestParam(name="keyword") 
      @NotNull(message = "Keyword must not be null") 
  		String keyword)
      {
    	  keywordGet = null;
    	  keywordPost = null;
      	try {
      		keywordGet = keywordService.getKeywordById(id);
      		if(keywordGet!=null) {
      			keywordPost = new Keyword(
      					keywordGet.get().getKeywordId(),
      	  				keyword,
      	  				keywordGet.get().getSongs(),
      	  				keywordGet.get().getUserPreferences()
      					);
      			return keywordService.saveKeyword(keywordPost);
      			}
      		else
    			return GenericResponse.error("NOT FOUND");
      	}catch(Exception ex) {
      		return GenericResponse.error(ex.getLocalizedMessage());
      	}
      }
    
      
      //DELETE KEYWORD DETAILS
	  //http://localhost:8080/yelody/keyword/deleteKeyword?id=
      @CrossOrigin(origins = "*")
      @DeleteMapping("/deleteKeyword")
      public GenericResponse deleteKeyword(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
    	  keywordGet = null;
      	try {
      		keywordGet = keywordService.getKeywordById(id);
      		if(keywordGet!=null) {
      			keywordService.deleteKeyword(keywordGet.get());
      			return GenericResponse.success("KEYWORD Id: " + id + " deleted Successfully");
      		}
      		else
    			return GenericResponse.error("NOT FOUND.");
      	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
      	}
      }
}
