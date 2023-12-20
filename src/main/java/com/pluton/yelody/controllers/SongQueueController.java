package com.pluton.yelody.controllers;

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
import com.pluton.yelody.models.SongQueue;
import com.pluton.yelody.models.SongQueueItem;
import com.pluton.yelody.services.SongQueueService;
import com.pluton.yelody.services.SongService;

@RestController
@RequestMapping("/yelody/queue")
public class SongQueueController {

    @Autowired
    SongQueueService songQueueService;
    @Autowired
    SongService songService;

    // Add song to the queue
    //http://localhost:8080/yelody/queue/addSongToQueue
  	@CrossOrigin(origins = "*")
  	@PostMapping("/addSongToQueue")
    public GenericResponse<SongQueueItem> addSongToQueue(@RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId,
			@RequestParam(name="songId") @org.hibernate.validator.constraints.UUID UUID songId) {
        try {
	  		SongQueueItem addedItem = songQueueService.addSongToQueue(userId, songService.getSongById(songId).get());
	        if (addedItem != null) 
	        	return GenericResponse.success(addedItem);
	        return GenericResponse.error("Failed to add song to queue because it is already existed");
        }catch(Exception ex) {
        	ex.printStackTrace();
    		return GenericResponse.error(ex.getLocalizedMessage());
        }
    }

  	//Remove song from the queue
    //http://localhost:8080/yelody/queue/deleteSong
    @CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteSong")
    public GenericResponse deleteSong(@RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId,
    		@RequestParam(name="songId") @org.hibernate.validator.constraints.UUID UUID songId){
    	try {
    		boolean success = songQueueService.removeSongFromQueue(userId, songService.getSongById(songId).get());
            if (success) {
            	return GenericResponse.success("Song removed from queue");
            } else {
            	return GenericResponse.error("Failed to remove song from queue");
            }
    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }

    // Reorder songs in the queue
    //http://localhost:8080/yelody/queue/reorderSongs
    @PutMapping("/reorderSongs")
    public GenericResponse reorderSongs(@RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId,
    		@RequestParam(name="songId") @org.hibernate.validator.constraints.UUID UUID reorderedSongs,
    		@RequestParam(name="position") int position) {
        try {
	    	boolean success = songQueueService.reorderSongInQueue(userId, songService.getSongById(reorderedSongs).get(), position);
	        if (success) {
	        	return GenericResponse.success("Songs reordered successfully");
	        } else {
	        	return GenericResponse.error("Failed to reorder songs");
	        }
        } catch (Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
        } 
    }

    // Get user's current song queue
    //http://localhost:8080/yelody/queue/getSongQueue?id=
    @GetMapping("/getSongQueue")
    public GenericResponse<SongQueue> getSongQueue(@RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId) {
    	try {
	        SongQueue userQueue = songQueueService.getUserSongQueue(userId).get();
	        if (userQueue != null) {
	        	return GenericResponse.success(userQueue);
	        } else {
	        	return GenericResponse.error("NOT FOUND");
	        }
    	}catch (Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
        }
    }
}
