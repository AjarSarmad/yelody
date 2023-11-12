package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.DeleteUserSingHistoryRequest;
import com.pluton.yelody.DTOs.UserSingHistoryRequest;
import com.pluton.yelody.models.User;
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
    		if(!StringUtils.getFilenameExtension(userSingHistoryRequest.getFile().getOriginalFilename()).equalsIgnoreCase("mp3"))
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SONG FILE FORMAT SHOULD BE .MP3");

    		userSingHistoryPost = new UserSingHistory(
    				UUID.randomUUID(),
    				userService.getUserByID(userSingHistoryRequest.getUserId()).get(),
    				songService.getSongById(userSingHistoryRequest.getSongId()).get(),
    				new java.sql.Date(System.currentTimeMillis()),
    				0L,
    				userSingHistoryRequest.getLyrics()
    				);
    		
    		UserSingHistory response = userSingHistoryService.postSingHistory(userSingHistoryPost);
    		if(response!=null) {
    			backblazeResponse = backblazeService.uploadSong(true, response.getSingHistoryId().toString(), userSingHistoryRequest.getFile());
    			if(backblazeResponse)
    				return new ResponseEntity<>(response, HttpStatus.CREATED);
    		}
    	}catch(Exception ex) {
    		ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
    	}
    	return null;
	}
	
	//DELETE USER SING HISTORY
    //http://localhost:8080/yelody/singHistory/deleteSingHistory
    @CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteSingHistory")
    public ResponseEntity<?> deleteSingHistory(@RequestBody DeleteUserSingHistoryRequest deleteUserSingHistoryRequest){
    	try {
    		if(deleteUserSingHistoryRequest!=null)
    			userSingHistoryService.deleteSingHistory(deleteUserSingHistoryRequest.getSingHistoryIds());
			return ResponseEntity.status(HttpStatus.OK).body("SING HISTORY HAS BEEN DELETED SUCCESSFULLY");
    	}catch(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
    	}
    }
    
    //GET LIST OF SING HISTORY
	//http://localhost:8080/yelody/singHistory/getAllSingHistories
	@CrossOrigin(origins = "*")
		@GetMapping("/getAllSingHistories")
	  public ResponseEntity<Object> getAllSingHistories() {
		userSingHistoryList = new ArrayList<>();
		try {
			userSingHistoryList = userSingHistoryService.getSingHistoryList();
			if(userSingHistoryList!=null)
				return new ResponseEntity<Object>(userSingHistoryList, HttpStatus.OK);
			return null;
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
	}
	
	//GET LIST OF SING HISTORY BY USER ID
	//http://localhost:8080/yelody/singHistory/getUserSingHistories?id=
	@CrossOrigin(origins = "*")
	@GetMapping("/getUserSingHistories")
    public ResponseEntity<Object> getUserSingHistories(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID userId) {
		userSingHistoryList = new ArrayList<>();
		Optional<User> userGet = null;
		try {
			userGet = userService.getUserByID(userId);
			if(userGet!=null)
				userSingHistoryList = userSingHistoryService.getUserSingHistories(userGet.get());
			return new ResponseEntity<Object>(userSingHistoryList, HttpStatus.OK);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
		}
	}
	
}
