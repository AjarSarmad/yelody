package com.pluton.yelody.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.User;
import com.pluton.yelody.repositories.GenreRepository;

@RestController
@RequestMapping("/yelody/genre")
public class GenreController {
		@Autowired
		GenreRepository genreRepository;
		
		List<User> genre = null;
		Optional<Genre> genreGet = null;
		Genre genrePost = null;
		
		//POST GENRE
	  	//http://localhost:8080/yelody/genre/postGenre?type=
	    @CrossOrigin(origins = "*")
	  	@PostMapping("/postGenre")
	    public ResponseEntity<Object> postGenre(@RequestParam(name="type") String type){
	    	try {
	  			return new ResponseEntity<Object>(genreRepository.save(new Genre(UUID.randomUUID(),type, null)), HttpStatus.CREATED);
	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	    	}
	    }
	    
	    //GET GENRE LIST
	    //http://localhost:8080/yelody/genre/listGenre
	    @CrossOrigin(origins = "*")
	  	@GetMapping("/listGenre")
	    public ResponseEntity<Object> listGenre(){
	    	try {
	    		return new ResponseEntity<Object>(genreRepository.findAll(), HttpStatus.OK);
	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	    	}
	    }
	    	
}
