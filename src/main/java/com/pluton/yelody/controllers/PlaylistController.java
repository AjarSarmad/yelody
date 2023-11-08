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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.AddSongsToPlaylistRequest;
import com.pluton.yelody.DTOs.DeleteSongsFromPlaylistRequest;
import com.pluton.yelody.DTOs.PlaylistPostRequest;
import com.pluton.yelody.models.Playlist;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.services.PlaylistService;
import com.pluton.yelody.services.SongService;
import com.pluton.yelody.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/playlist")
public class PlaylistController {
	@Autowired
	PlaylistService playlistService;
	@Autowired
	UserService userService;
	@Autowired
	SongService songService;
	
	List<Playlist> playlist = null;
	Optional<Playlist> playlistGet = null;
	Playlist playlistPost = null;
	List<Song> songList = null;
	
	//POST USER PLAYLIST
	//http://localhost:8080/yelody/playlist/postPlaylist
	@CrossOrigin(origins = "*")
  	@PostMapping("/postPlaylist")
    public ResponseEntity<Object> postPlaylist( @RequestBody @Valid PlaylistPostRequest playlistPostRequest)throws IllegalArgumentException {
		playlistPost = null;
		songList = new ArrayList<>();
    	try {
    		if(playlistPostRequest.getSongIds()!=null && !playlistPostRequest.getSongIds().isEmpty()) {
	    		for(UUID songId: playlistPostRequest.getSongIds()) {
	    			songList.add(songService.getSongById(songId).get());
	    		}
    		}
    		
    		playlistPost = new Playlist(
    				null,
    				userService.getUserByID(playlistPostRequest.getUserId()).get(),
    				songList,
    				new java.sql.Date(System.currentTimeMillis()),
    				false
    				);
    		
    		Playlist response = playlistService.postPlaylist(playlistPost);
    		if(response!=null) {
    				return new ResponseEntity<>(response, HttpStatus.CREATED);
    		}
    		return null;
    	}catch(Exception ex) {
    		ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
    	}
	}
	
	//POST SONGS TO PLAYLIST
	//http://localhost:8080/yelody/playlist/postSongsToPlaylist
	@CrossOrigin(origins = "*")
  	@PutMapping("/postSongsToPlaylist")
    public ResponseEntity<Object> postSongsToPlaylist( @RequestBody @Valid AddSongsToPlaylistRequest addSongsToPlaylistRequest){
		playlistGet = null;
		songList = new ArrayList<>();
    	try {
			playlistGet = playlistService.getPlaylistById(addSongsToPlaylistRequest.getPlaylistId());
			playlistService.addSongsToPlaylist(playlistGet.get(), addSongsToPlaylistRequest.getSongIds());
			return ResponseEntity.status(HttpStatus.CREATED).body("SONGS HAVE SUCCESSFULLY ADDED TO THE PLAYLIST ID: " + addSongsToPlaylistRequest.getPlaylistId());
    	}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
    	}
	}
	
	
	//GET LIST OF PLAYLISTS
	//http://localhost:8080/yelody/playlist/getAllPlaylists
	@CrossOrigin(origins = "*")
  	@GetMapping("/getAllPlaylists")
    public ResponseEntity<Object> getAllPlaylists() {
		playlist = new ArrayList<>();
		try {
			playlist = playlistService.getAllPlaylist();
			if(playlist!=null)
				return new ResponseEntity<Object>(playlist, HttpStatus.OK);
			return null;
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
	}

	//GET PLAYLISTS BY USER ID
	//http://localhost:8080/yelody/playlist/getPlaylistByUserId?id=
	@CrossOrigin(origins = "*")
  	@GetMapping("/getPlaylistByUserId")
    public ResponseEntity<Object> getPlaylistByUserId(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id,@RequestParam(required = false, name = "isFavourite") boolean isFavourite) {
		playlist = new ArrayList<>();
		try {
			if(userService.getUserByID(id)!=null) {
				if(isFavourite)
					playlist = playlistService.getFavouritePlaylistByUserId(id);
				else
					playlist = playlistService.getPlaylistByUserId(id);
			if(playlist!=null)
				return new ResponseEntity<Object>(playlist, HttpStatus.OK);
			}
			return null;
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
	}
	
	//GET PLAYLIST BY PLAYLIST_ID
	//http://localhost:8080/yelody/playlist/getPlaylistById?id=
	@CrossOrigin(origins = "*")
  	@GetMapping("/getPlaylistById")
    public ResponseEntity<Object> getPlaylistById(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) {
		playlistGet = null;
		try {
			playlistGet = playlistService.getPlaylistById(id);
			if(playlistGet!=null)
				return new ResponseEntity<Object>(playlistGet.get(), HttpStatus.OK);
			return null;
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
		}
	}
	
	//DELETE SONGS FROM PLAYLIST
	//http://localhost:8080/yelody/playlist/deleteSongsFromPlaylist
	@CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteSongsFromPlaylist")
    public ResponseEntity<Object> deleteSongsFromPlaylist(@RequestBody @Valid DeleteSongsFromPlaylistRequest deleteSongsFromPlaylistRequest) {
		songList = new ArrayList<>();
		playlistGet = null;
		try{
			playlistGet = playlistService.getPlaylistById(deleteSongsFromPlaylistRequest.getPlaylistId());
			if(playlistGet!=null)
				playlistService.deleteSongsFromPlayList(playlistGet.get(), deleteSongsFromPlaylistRequest.getSongIds());

			return ResponseEntity.status(HttpStatus.OK).body("SONGS DELETION IS SUCCESSFUL");

		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
		}
	}
	
	//DELETE PLAYLIST BY PLAYLIST_ID
	//http://localhost:8080/yelody/playlist/deletePlaylistById?id=
	@CrossOrigin(origins = "*")
	@DeleteMapping("/deletePlaylistById")
    public ResponseEntity<Object> deletePlaylistById(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) {
		playlistGet = null;
		try {
			playlistGet = playlistService.getPlaylistById(id);
			if(playlistGet!=null) {
				playlistService.deletePlaylist(playlistGet.get());
				return ResponseEntity.status(HttpStatus.OK).body("PLAYLIST ID:" + id + " Deleted Successfully");
			}
			else
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PLAYLIST ID: " + id + " NOT FOUND");
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
		}
	}
	
	//ADD OR REMOVE FROM FAVOURITE
	//http://localhost:8080/yelody/playlist/addRemoveFavourite?id=
	@CrossOrigin(origins = "*")
  	@PutMapping("/addRemoveFavourite")
    public ResponseEntity<Object> addRemoveFavourite(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID playlistId){
		playlistGet = null;
    	try {
			playlistGet = playlistService.getPlaylistById(playlistId);
			if(playlistGet!=null)
				playlistService.addRemoveFavourite(playlistGet.get());
			return ResponseEntity.status(HttpStatus.CREATED).body("PLAYLIST HAS BEEN " + (playlistGet.get().isFavourite() ? "MARKED" : "REMOVED") + " FAVORITE");
    	}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
    	}
	}
	
}
