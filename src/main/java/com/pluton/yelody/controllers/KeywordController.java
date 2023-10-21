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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.services.KeywordService;

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
	public ResponseEntity<Object> addKeyword(@RequestParam(name="keyword")
	@NotNull(message = "Keyword must not be null") 
	String keyword){
		keywordPost = null;
		try {
				keywordPost = new Keyword(
	            UUID.randomUUID(),
	            keyword,
	            null);
			
			return keywordService.saveKeyword(keywordPost);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.FOUND).body("KEYWORD Already Exists");
		}
	}
	
	//GET KEYWORD DETAILS BY ID
	//http://localhost:8080/yelody/keyword/getKeywordById?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getKeywordById")
    public ResponseEntity<Object> getKeywordById(@RequestParam(name="id") UUID id){
    	keywordGet = null;
    	try {
    		keywordGet = keywordService.getKeywordById(id);
    		if(keywordGet!=null)
    			return new ResponseEntity<Object>(keywordGet.get(), HttpStatus.OK);
    		else
      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    
      //GET LIST OF KEYWORDs
	  //http://localhost:8080/yelody/keyword/getKeywordList
      @CrossOrigin(origins = "*")
	  @GetMapping("/getKeywordList")
	  public ResponseEntity<Object> getKeywordList(){
    	  keywordList = null;
	  	try {
	  		keywordList = new ArrayList<>(keywordService.getKeywordList());
	  		if(keywordList!=null)
	  			return new ResponseEntity<Object>(keywordList, HttpStatus.OK);
	  		else
	    			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	
	  	}catch(Exception ex) {
				return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	  	}
	  }
      
      
      //UPDATE KEYWORD DETAILS
	  //http://localhost:8080/yelody/keyword/updateKeyword?id=&keyword=
      @CrossOrigin(origins = "*")
      @PutMapping("/updateKeyword")
      public ResponseEntity<Object> updateKeyword(@RequestParam(name="id")
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
      	  				keywordGet.get().getSongs()
      					);
      			return new ResponseEntity<Object>(keywordService.saveKeyword(keywordPost), HttpStatus.OK);
      			}
      		else
      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
      	}catch(Exception ex) {
  			ex.printStackTrace();
  			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("KEYWORD NOT FOUND");
      	}
      }
    
      
      //DELETE KEYWORD DETAILS
	  //http://localhost:8080/yelody/keyword/deleteKeyword?id=
      @CrossOrigin(origins = "*")
      @DeleteMapping("/deleteKeyword")
      public ResponseEntity<Object> deleteKeyword(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
    	  keywordGet = null;
      	try {
      		keywordGet = keywordService.getKeywordById(id);
      		if(keywordGet!=null) {
      			keywordService.deleteKeyword(keywordGet.get());
      			return new ResponseEntity<Object>(HttpStatus.OK);
      		}
      		else
      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
      	}catch(Exception ex) {
  			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("KEYWORD NOT FOUND");
      	}
      }
}
