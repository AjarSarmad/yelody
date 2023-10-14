package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.SongRequest;
import com.pluton.yelody.services.SongService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/song")
public class SongController {
	@Autowired
	SongService songService;
//	@Autowired
//	GenreRepository genreRepository;
	
	List<Song> songList = null;
	Song song = null;
	Optional<Genre> genre = null;
	
	//POST A SONG
	//http://localhost:8080/yelody/song/postSong
    @CrossOrigin(origins = "*")
  	@PostMapping("/postSong")
    public ResponseEntity<Object> postUser( @RequestBody @Valid SongRequest songRequest){
    	song = null;
    	try {
//    		if(genreRepository.existsByType(songRequest.getGenre())) {
//    			genre = genreRepository.findByType(songRequest.getGenre());
    		
  				song = new Song(
  						UUID.randomUUID(),
  						songRequest.getUserName(),
  						songRequest.getDescription(),
  						songRequest.getRank(),
  						songRequest.getArtistName(),
  						songRequest.getLyrics(),
  						0,
  						null,
  						null,
//  						genre.get(),
  						null,
  						null);
  				
  				return songService.postSong(song);
//    		}else
//      			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }

    
	//List of SONGS
	//http://localhost:8080/yelody/song/listSongs
	@CrossOrigin(origins = "*")
	@GetMapping("/listSongs")
	public ResponseEntity<Object> listSongs() {
		songList = new ArrayList<>();
		try {
			songList = songService.getSongList();
			if(songList.isEmpty())
	            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			else
	            return new ResponseEntity<Object>(songList, HttpStatus.OK);

		}catch(Exception ex) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
		}
	}
	
	//INCREMENT VIEW_COUNT OF SONG BY ID
	//http://localhost:8080/yelody/song/incrementViewById?id=
	@CrossOrigin(origins = "*")
	@PostMapping("/incrementViewById")
	public ResponseEntity<Object> incrementViewById( @RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) {
        return new ResponseEntity<Object>(songService.incrementViewCount(id));
	}
	       

	
}
