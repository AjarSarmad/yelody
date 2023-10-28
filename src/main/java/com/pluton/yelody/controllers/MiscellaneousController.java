package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.MiscellaneousResponse;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.services.AgeGroupService;
import com.pluton.yelody.services.GenreService;
import com.pluton.yelody.services.KeywordService;

@RestController
@RequestMapping("/yelody/miscellaneous")
public class MiscellaneousController {
	@Autowired
	AgeGroupService ageGroupService;
	@Autowired
	GenreService genreService;
	@Autowired
	KeywordService keywordService;
	
	List<Keyword> keywordList = null;
	List<AgeGroup> ageGroupList = null;
	List<Genre> genreList = null;
	
	//GET AGEGROUP, KEYWORD & GENRE
	//http://localhost:8080/yelody/miscellaneous/age_key_genre
	@CrossOrigin(origins = "*")
	@GetMapping("/age_key_genre")
	public ResponseEntity<Object> age_key_genre() {
		keywordList = new ArrayList<>();
		ageGroupList = new ArrayList<>();
		genreList = new ArrayList<>();
		try {
			keywordList = keywordService.getKeywordList();
			ageGroupList = ageGroupService.getAgeGroupList();
			genreList = genreService.getGenreList();
			
			return new ResponseEntity<Object>(
					new MiscellaneousResponse(
							ageGroupList,
							keywordList,
							genreList
							),
					HttpStatus.OK
					);
		}catch(Exception ex) {
  			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
	}
}
