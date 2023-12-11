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

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;
import com.pluton.yelody.services.GenreService;
import com.pluton.yelody.services.KeywordService;
import com.pluton.yelody.services.UserPreferenceService;
import com.pluton.yelody.services.UserService;

@RestController
@RequestMapping("/yelody/userpreferences")
public class UserPreferenceController {
	@Autowired
	UserPreferenceService userPreferencesService;
	@Autowired
	UserService userService;
	@Autowired
	GenreService genreService;
	@Autowired
	KeywordService keywordService;

	List<UserPreferences> userPreferencesList = null;
	Optional<UserPreferences> userPreferencesGet = null;
	UserPreferences userPreferencesPost = null;

	// POST USER PREFERENCE
	// http://localhost:8080/yelody/userpreferences/postUserPreference?userId=xxx&genreId=yyy&keywordId=zzz
	@CrossOrigin(origins = "*")
	@PostMapping("/postUserPreference")
	public GenericResponse<UserPreferences> postUserPreference(
			@org.hibernate.validator.constraints.UUID @RequestParam(name="userId") UUID userId,
			@org.hibernate.validator.constraints.UUID @RequestParam(name="genreId", required=false) UUID genreId,
			@org.hibernate.validator.constraints.UUID @RequestParam(name="keywordId", required=false) UUID keywordId)
	{
		userPreferencesPost = null;
		
		if (genreId == null && keywordId == null) {
			return GenericResponse.error("Either genreId or keywordId must be provided.");
	    }
		
		try {
			User user = userService.getUserByID(userId).get();
			if(user!=null) {
				userPreferencesPost = new UserPreferences(null,
						user,
						genreId!=null? genreService.getGenreByID(genreId).get():null,
						keywordId!=null? keywordService.getKeywordById(keywordId).get():null
						); 
			}
				return userPreferencesService.createUserPreference(userPreferencesPost);
		} catch (Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}
	}

	// GET USER PREFERENCE LIST BY USER ID
	// http://localhost:8080/yelody/userpreferences/listPreferencesByUserId?userId=xxx
	@CrossOrigin(origins = "*")
	@GetMapping("/listPreferencesByUserId")
	public GenericResponse<List<UserPreferences>> listPreferencesByUserId(@RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId) {
		userPreferencesList = new ArrayList<>();
	    try {
	    	userPreferencesList = userPreferencesService.getUserPreferencesByUserId(userService.getUserByID(userId).get());
	        if (userPreferencesList != null && !userPreferencesList.isEmpty()) {
	        	return GenericResponse.success(userPreferencesList);
	        } else {
	        	return GenericResponse.error("No preferences found for the given user ID.");
	        }
	    } catch (Exception ex) {
	    	return GenericResponse.error(ex.getLocalizedMessage());
	    }
	}


	// EDIT A USER PREFERENCE BY ID
	// http://localhost:8080/yelody/userpreferences/updatePreference?id=xxx&userId=yyy&genreId=zzz&keywordId=aaaa
	@CrossOrigin(origins = "*")
	@PutMapping("/updatePreference")
	public GenericResponse<UserPreferences> updatePreference(
		@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id,
		@RequestParam(name="genreId", required=false) @org.hibernate.validator.constraints.UUID UUID genreId,
		@RequestParam(name="keywordId", required=false) @org.hibernate.validator.constraints.UUID UUID keywordId)
	{
		userPreferencesGet = null;
		
		if (genreId == null && keywordId == null) {
			return GenericResponse.error("Either genreId or keywordId must be provided.");
	    }
		
		try {
			userPreferencesGet = userPreferencesService.getUserPreferencesByID(id);
			if (userPreferencesGet.isPresent()) {
				UserPreferences existingPreference = userPreferencesGet.get();
				existingPreference.setGenre(genreId!=null? genreService.getGenreByID(genreId).get():null);
				existingPreference.setKeyword(keywordId!=null? keywordService.getKeywordById(keywordId).get(): null);
				return userPreferencesService.createUserPreference(existingPreference);
			} else {
				return GenericResponse.error("NOT FOUND!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return GenericResponse.error(ex.getLocalizedMessage());
		}
	}

	// DELETE A USER PREFERENCE BY ID
	// http://localhost:8080/yelody/userpreferences/deletePreference?id=xxx
	@CrossOrigin(origins = "*")
	@DeleteMapping("/deletePreference")
	public GenericResponse deletePreference(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) {
		userPreferencesGet = null;
		try {
			userPreferencesGet = userPreferencesService.getUserPreferencesByID(id);
			if (userPreferencesGet.isPresent()) {
				userPreferencesService.deleteUserPreferences(userPreferencesGet.get());
				return GenericResponse.success("User Preference ID:" + id + " Deleted Successfully");
			} else {
				return GenericResponse.error("User Preference ID: " + id + " NOT FOUND");
			}
		} catch (Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}
	}

}
