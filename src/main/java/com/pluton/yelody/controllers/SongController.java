package com.pluton.yelody.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.SongConverter;
import com.pluton.yelody.DTOs.SongCriteriaSearch;
import com.pluton.yelody.DTOs.SongMapper;
import com.pluton.yelody.DTOs.SongRequest;
import com.pluton.yelody.DTOs.SongResponse;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.models.Chart;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.User;
import com.pluton.yelody.services.AgeGroupService;
import com.pluton.yelody.services.BackblazeService;
import com.pluton.yelody.services.ChartService;
import com.pluton.yelody.services.GenreService;
import com.pluton.yelody.services.KeywordService;
import com.pluton.yelody.services.SongService;
import com.pluton.yelody.services.UserPreferenceService;
import com.pluton.yelody.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/song")
public class SongController {
	@Autowired
	SongService songService;
	@Autowired
	GenreService genreService;
	@Autowired
	UserService userService;
	@Autowired
	AgeGroupService ageGroupService;
	@Autowired
	KeywordService keywordService;
	@Autowired
	ChartService chartService;
	@Autowired
	BackblazeService backblazeService;
	@Autowired
	UserPreferenceService userPreferenceService;
	@Autowired
	SongMapper songMapper;
	@Autowired
	SongConverter songConverter;
	
	String imagePath = "Resources/IMAGE/SONG";
	List<Song> songList = null;
	Song song = null;
	Optional<Song> songGet = null;
	Optional<Genre> genre = null;
	Optional<Keyword> keyword = null;
	Optional<Chart> chart = null;
	Optional<User> user = null;
	Optional<AgeGroup> ageGroup = null;
	boolean backblazeResponse = false;
	
	//POST A SONG
	//http://localhost:8080/yelody/song/postSong
	@CrossOrigin(origins = "*")
	@PostMapping("/postSong")
	public GenericResponse<Song> postSong(@ModelAttribute @Valid SongRequest songRequest) throws IOException {
	    
	    try {
	    	if(songService.validateSongRequest(songRequest)) {
	        
	        Song song = songConverter.convertRequestToEntity(songRequest);

	        if (song == null)
	        	return GenericResponse.error("Missing necessary song data.");
	        
	        // Save song and upload to backblaze
	        Song songPost = songService.postSong(song);
	        if (songPost != null) {
	            boolean backblazeResponse = backblazeService.uploadSong(false, songPost.getSongId().toString(), songRequest.getFile());
	            if (backblazeResponse)
	            	return GenericResponse.success(songPost, "SONG CREATED SUCCESSFULLY"); 
	        }
    	}
	    } catch (Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
	    }
	    return GenericResponse.error("Error processing song.");
	}

    //List of SONGS
  	//http://localhost:8080/yelody/song/listSongs
    @CrossOrigin(origins = "*")
    @PostMapping("/listSongs")
    public GenericResponse<List<SongResponse>> listSongs(@RequestBody(required = false) @Valid SongCriteriaSearch songCriteriaSearch) {
        List<SongResponse> songResponseList = new ArrayList<>();
        try {
            List<Song> songList = songService.getSongsBasedOnCriteria(songCriteriaSearch);
            
            for (Song item : songList) {
                songResponseList.add(songMapper.songToSongResponse(item, false));
            }

            if (songResponseList.isEmpty()) {
            	return GenericResponse.error("NO SONG EXISTS");
            } else {
            	return GenericResponse.success(songResponseList);
            }
        } catch (Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
        }
    }
    
    
	//INCREMENT VIEW_COUNT OF SONG BY ID
	//http://localhost:8080/yelody/song/incrementViewById?userId=&&songId=
	@CrossOrigin(origins = "*")
	@PostMapping("/incrementViewById")
	public GenericResponse incrementViewById( @RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId,
			@RequestParam(name="songId") @org.hibernate.validator.constraints.UUID UUID songId) {
        return songService.incrementViewCount(userId, songId);
	}
	
	//DELETE SONG
    //http://localhost:8080/yelody/song/deleteSong?id=
    @CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteSong")
    public GenericResponse deleteSong(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
    	songGet = null;
    	try {
    		songGet = songService.getSongById(id);
    		if(songGet!=null)
    			return songService.deleteSong(songGet.get());
    		return GenericResponse.error("SONG " + id + " NOT FOUND");
    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
	
    // GET RECOMMENDED SONGS BASED ON USER PREFERENCES
    // http://localhost:8080/yelody/song/getRecommendedSongs?userId=xxx
    @CrossOrigin(origins = "*")
    @GetMapping("/getRecommendedSongs")
    public GenericResponse<List<SongResponse>> getRecommendedSongs(@RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId) {
        List<SongResponse> songResponseList = new ArrayList<>();
    	try {
            User user = userService.getUserByID(userId).get();
            List<Song> recommendedSongs = songService.getRecommendedSongsForUser(user);
            for (Song item : recommendedSongs) {
                songResponseList.add(songMapper.songToSongResponse(item, false));
            }

            if (songResponseList.isEmpty()) {
            	return GenericResponse.error("No songs found based on user preferences.");
            } else {
            	return GenericResponse.success(songResponseList);
            }
           
        } catch (Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
        }
    }
    
	 // GET SONG DETAILS BY ID
	 // http://localhost:8080/yelody/song/getSongById?id=
	 @CrossOrigin(origins = "*")
	 @GetMapping("/getSongById")
	    public GenericResponse<SongResponse> getSongById(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) {
	        try {
	            Optional<Song> optionalSong = songService.getSongById(id);
	            if (optionalSong.isPresent()) {
	                Song song = optionalSong.get();
	                SongResponse songResponse = songMapper.songToSongResponse(song,true);
	                return GenericResponse.success(songResponse);
	            } else {
	            	return GenericResponse.error("Song not found for ID: " + id);
	            }
	        } catch (Exception ex) {
	    		return GenericResponse.error(ex.getLocalizedMessage());
	        }
	    }
}
