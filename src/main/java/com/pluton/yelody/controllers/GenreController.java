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
	    public GenericResponse<Genre> postGenre(@RequestParam(name="type") String type){
	    	genrePost = null;
	    	try {
	    		genrePost = new Genre(UUID.randomUUID(),type, null, null);
	  			return genreService.createGenre(genrePost);
	    	}catch(Exception ex) {
	    		return GenericResponse.error(ex.getLocalizedMessage());
	    	}
	    }
	    
		 // GET GENRE DETAILS BY ID
		 // http://localhost:8080/yelody/genre/getGenreById?id=
		 @CrossOrigin(origins = "*")
		 @GetMapping("/getGenreById")
		 public GenericResponse<Genre> getGenreById(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) {
		     Genre genre = null;
		     try {
		         Optional<Genre> optionalGenre = genreService.getGenreByID(id);
		         if (optionalGenre.isPresent()) {
		             genre = optionalGenre.get();
		             return GenericResponse.success(genre);
		         } else {
		        	 return GenericResponse.error("Genre not found for ID: " + id);
		         }
		     } catch (Exception ex) {
		    		return GenericResponse.error(ex.getLocalizedMessage());
		     }
		 }
	    
	    //GET GENRE LIST
	    //http://localhost:8080/yelody/genre/listGenre
	    @CrossOrigin(origins = "*")
	  	@GetMapping("/listGenre")
	    public GenericResponse<List<Genre>> listGenre(){
	    	genreList = new ArrayList<>();
	    	try {
	    		genreList = genreService.getGenreList();
	    		
	    		if(genreList!=null ||!(genreList.isEmpty()))
	    			return GenericResponse.success(genreList);
	    		else
	    			return GenericResponse.error("NOT FOUND");
	    	}catch(Exception ex) {
	    		return GenericResponse.error(ex.getLocalizedMessage());
	    	}
	    }
	    
	    //EDIT A GENRE BY ID
	    //http://localhost:8080/yelody/genre/updateGenre?id=xxx&type=xx
	    @CrossOrigin(origins = "*")
	  	@PutMapping("/updateGenre")
	    public GenericResponse<Genre> updateGenre(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id, @RequestParam(name="type") String type){
	    	genrePost = null;
	    	genreGet = null;
	    	try {
	    		genreGet = genreService.getGenreByID(id);
	    		if(genreGet!=null) {
		    		genrePost = new Genre(genreGet.get().getGenreId() , type , genreGet.get().getSongs(), genreGet.get().getUserPreferences());
		    		return genreService.createGenre(genrePost);
    			}
	    		else
	    			return GenericResponse.error("NOT FOUND");

	    	}catch(Exception ex) {
				ex.printStackTrace();
				return GenericResponse.error(ex.getLocalizedMessage());
	    	}
	    }
	    
	    
	    //DELETE A GENRE BY ID
	    //http://localhost:8080/yelody/genre/deleteGenre?id=
	    @CrossOrigin(origins = "*")
	  	@DeleteMapping("/deleteGenre")
	    public GenericResponse deleteGenre(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
	    	genreGet = null;
	    	try {
	    		genreGet = genreService.getGenreByID(id);
	    		if(genreGet!=null) {
	    			genreService.deleteGenre(genreGet.get());
	    			return GenericResponse.success("GENRE ID:" + id + " Deleted Successfully");
	    		}
	    		else
	    			return GenericResponse.error("GENRE ID: " + id + " NOT FOUND");

	    	}catch(Exception ex) {
	    		return GenericResponse.error(ex.getLocalizedMessage());
	    	}
	    }	
}
