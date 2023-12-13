package com.pluton.yelody.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.UserPrefrenceRequest;
import com.pluton.yelody.DTOs.UserPrefrenceResponse;
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
	// http://localhost:8080/yelody/userpreferences/postUserPreference
	@CrossOrigin(origins = "*")
	@PostMapping("/postUserPreference")
	public GenericResponse<UserPrefrenceResponse> postUserPreference(@RequestBody UserPrefrenceRequest userPrefrenceRequest)
	{
		userPreferencesPost = null;
		
		try {
			User user = userService.getUserByID(userPrefrenceRequest.getUserId()).get();
			if(user!=null) {
				return userPreferencesService.createUserPreference(user, userPrefrenceRequest);
			}
		} catch (Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}
    	return GenericResponse.error();
	}

	// GET USER PREFERENCE LIST BY USER ID
	// http://localhost:8080/yelody/userpreferences/listPreferencesByUserId?userId=xxx
	@CrossOrigin(origins = "*")
	@GetMapping("/listPreferencesByUserId")
	public GenericResponse<UserPrefrenceResponse> listPreferencesByUserId(@RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId) {
	    try {
	    	return userPreferencesService.getUserPreferenceResponseByUserId(userService.getUserByID(userId).get(),"SUCCESS");
	    } catch (Exception ex) {
	    	return GenericResponse.error(ex.getLocalizedMessage());
	    }
	}


	// EDIT A USER PREFERENCE BY ID
	// http://localhost:8080/yelody/userpreferences/updatePreference
	@CrossOrigin(origins = "*")
	@PutMapping("/updatePreference")
	public GenericResponse<UserPrefrenceResponse> updatePreference(@RequestBody UserPrefrenceRequest userPrefrenceRequest)
	{
		try {
			User user = userService.getUserByID(userPrefrenceRequest.getUserId()).get();
			if(user!=null) {
				return userPreferencesService.updateUserPreferenceByUser(user, userPrefrenceRequest);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return GenericResponse.error(ex.getLocalizedMessage());
		}
		return GenericResponse.error("NO PREFERENCE EXISTS");
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
