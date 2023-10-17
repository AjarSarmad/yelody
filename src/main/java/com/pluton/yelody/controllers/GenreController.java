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

import com.pluton.yelody.models.Genre;
import com.pluton.yelody.services.GenreService;

@RestController
@RequestMapping("/yelody/genre")
public class GenreController {
		@Autowired
		GenreService genreService;
		
		List<Genre> genreList = null;
		Optional<Genre> genreGet = null;
		Genre genrePost = null;
		
		//POST GENRE
	  	//http://localhost:8080/yelody/genre/postGenre?type=
	    @CrossOrigin(origins = "*")
	  	@PostMapping("/postGenre")
	    public ResponseEntity<Object> postGenre(@RequestParam(name="type") String type){
	    	genrePost = null;
	    	try {
	    		genrePost = new Genre(UUID.randomUUID(),type, null);
	  			return genreService.createGenre(genrePost);
	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	    	}
	    }
	    
	    //GET GENRE LIST
	    //http://localhost:8080/yelody/genre/listGenre
	    @CrossOrigin(origins = "*")
	  	@GetMapping("/listGenre")
	    public ResponseEntity<Object> listGenre(){
	    	genreList = new ArrayList<>();
	    	try {
	    		genreList = genreService.getGenreList();
	    		
	    		if(genreList!=null ||!(genreList.isEmpty()))
		    		return new ResponseEntity<Object>(genreList, HttpStatus.OK);
	    		else
	      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	    	}
	    }
	    
	    //EDIT A GENRE BY ID
	    //http://localhost:8080/yelody/genre/updateGenre?id=xxx&type=xx
	    @CrossOrigin(origins = "*")
	  	@PutMapping("/updateGenre")
	    public ResponseEntity<Object> updateGenre(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id, @RequestParam(name="type") String type){
	    	genrePost = null;
	    	genreGet = null;
	    	try {
	    		genreGet = genreService.getGenreByID(id);
	    		if(genreGet!=null) {
		    		genrePost = new Genre(genreGet.get().getGenreId() , type , genreGet.get().getSongs());
		    		return new ResponseEntity<Object>(genreService.createGenre(genrePost), HttpStatus.OK);
    			}
	    		else
	    			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

	    	}catch(Exception ex) {
				ex.printStackTrace();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
	    	}
	    }
	    
	    
	    //DELETE A GENRE BY ID
	    //http://localhost:8080/yelody/genre/deleteGenre?id=
	    @CrossOrigin(origins = "*")
	  	@DeleteMapping("/deleteGenre")
	    public ResponseEntity<Object> deleteGenre(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
	    	genreGet = null;
	    	try {
	    		genreGet = genreService.getGenreByID(id);
	    		if(genreGet!=null) {
	    			genreService.deleteGenre(genreGet.get());
	    			return new ResponseEntity<Object>(HttpStatus.OK);
	    		}
	    		else
	    			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	    	}
	    }	
}
