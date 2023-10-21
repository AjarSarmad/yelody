package com.pluton.yelody.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.UserSingHistoryRequest;
import com.pluton.yelody.models.UserSingHistory;
import com.pluton.yelody.services.BackblazeService;
import com.pluton.yelody.services.SongService;
import com.pluton.yelody.services.UserService;
import com.pluton.yelody.services.UserSingHistoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/singHistory")
public class UserSingHistoryController {
	@Autowired
	UserSingHistoryService userSingHistoryService;
	@Autowired
	UserService userService;
	@Autowired
	SongService songService;
	@Autowired
	BackblazeService backblazeService;
	
	List<UserSingHistory> userSingHistoryList = null;
	Optional<UserSingHistory> userSingHistoryGet = null;
	UserSingHistory userSingHistoryPost = null;

	
	//POST USER SING HISTORY
	//http://localhost:8080/yelody/singHistory/postSingHistory
	@CrossOrigin(origins = "*")
  	@PostMapping("/postSingHistory")
    public ResponseEntity<Object> postSingHistory( @ModelAttribute @Valid UserSingHistoryRequest userSingHistoryRequest){
		userSingHistoryPost = null;
		boolean backblazeResponse = false;

    	try {
    		userSingHistoryPost = new UserSingHistory(
    				UUID.randomUUID(),
    				userService.getUserByID(userSingHistoryRequest.getUserId()).get(),
    				songService.getSongById(userSingHistoryRequest.getSongId()).get(),
    				new java.sql.Date(System.currentTimeMillis()),
    				0L
    				);
    		
    		UserSingHistory response = userSingHistoryService.postSingHistory(userSingHistoryPost);
    		if(response!=null) {
    			backblazeResponse = backblazeService.uploadSong(true, response.getSingHistoryId().toString(), userSingHistoryRequest.getFile());
    			if(backblazeResponse)
    				return new ResponseEntity<>(response, HttpStatus.CREATED);
    		}
    	}catch(Exception ex) {
    		ex.printStackTrace();
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    	return null;
	}
}
