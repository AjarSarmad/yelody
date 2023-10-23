package com.pluton.yelody.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.SongCriteriaSearch;
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
import com.pluton.yelody.services.UserService;
import com.pluton.yelody.utilities.ImageUtil;

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
	
	String imagePath = "ImageResources/SONG";
	List<Song> songList = null;
	Song song = null;
	Optional<Song> songGet = null;
	Optional<Genre> genre = null;
	Optional<Keyword> keyword = null;
	Optional<Chart> chart = null;
	Optional<User> user = null;
	Optional<AgeGroup> ageGroup = null;
	List<SongResponse> songResponseList = null;
	boolean backblazeResponse = false;
	
	//POST A SONG
	//http://localhost:8080/yelody/song/postSong
    @CrossOrigin(origins = "*")
  	@PostMapping("/postSong")
    public ResponseEntity<Object> postSong( @ModelAttribute @Valid SongRequest songRequest) throws IOException{
    	song = null;
    	genre = null;
    	keyword = null;
    	ageGroup = null;
    	chart = null;
		String imageResponse = null;

    	try {
			genre = genreService.getGenreByType(songRequest.getGenre());
			keyword = keywordService.getKeywordByName(songRequest.getKeyword());
			chart = chartService.getChartByName(songRequest.getChart());
			ageGroup = ageGroupService.getAgeGroupByName(songRequest.getAgeGroup());
    			
			UUID id = UUID.randomUUID();
			if(!songRequest.getImage().isEmpty())
				imageResponse = ImageUtil.saveFile(imagePath, id.toString(), songRequest.getImage());
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IMAGE CANNOT BE NULL");	
			
    		if(genre!=null && keyword!=null && chart!=null && ageGroup!=null) {
  				song = new Song(
  						id,
  						songRequest.getSongName(),
  						songRequest.getDescription(),
  						songRequest.getRank(),
  						songRequest.getArtistName(),
  						songRequest.getLyrics(),
  						imageResponse, //image
  						new ArrayList<Keyword>(), //keywords
  						new ArrayList<User>(), //viewers
  						genre.get(), //genre
  						chart.get(),
  						ageGroup.get(),
  						new ArrayList<User>()
  						);
  				song.getKeywordlist().add(keyword.get());

  				Song songPost = songService.postSong(song);
  				if(songPost!=null) {
  					backblazeResponse = backblazeService.uploadSong(false, songPost.getSongId().toString(),songRequest.getFile());
  					if(backblazeResponse) {
  			  			return new ResponseEntity<Object>(songPost, HttpStatus.CREATED); 
  					}
  				}
    		}else
      			return new ResponseEntity<>("Given Genre is not existed",HttpStatus.BAD_REQUEST);

    	}catch(Exception ex) {
    		ex.getMessage();
  			return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    	}
		return null;
    }

    
	//List of SONGS
	//http://localhost:8080/yelody/song/listSongs
	@CrossOrigin(origins = "*")
	@GetMapping("/listSongs")
	public ResponseEntity<Object> listSongs(@RequestBody(required = false) @Valid SongCriteriaSearch songCriteriaSearch) {
		songList = new ArrayList<>();
		try {
			if (songCriteriaSearch!=null && songCriteriaSearch.getFilterBy() != null && songCriteriaSearch.isDoFilter()) {
	            switch (songCriteriaSearch.getFilterBy().toLowerCase()) {
	                case "name":
	                	songList = songService.getSongByName(songCriteriaSearch.getFilter(), songCriteriaSearch.getSortBy() );
	                    break;
	                case "artistname":
	                	songList = songService.getSongByArtistName(songCriteriaSearch.getFilter(), songCriteriaSearch.getSortBy());
	                    break;
	                case "rank":
	                	songList = songService.getSongByRank(songCriteriaSearch.getFilter(), songCriteriaSearch.getSortBy());
	                    break;
	                case "genre":
	                	songList = songService.getSongByGenre(songCriteriaSearch.getFilter(), songCriteriaSearch.getSortBy());
	                    break;
//	                case "keyword":
//	                	songList = songService.getSongByKeyword(songCriteriaSearch.getFilter(), songCriteriaSearch.getSortBy());
//	                    break;
	                default:
	                    return new ResponseEntity<>("Invalid filterBy value", HttpStatus.BAD_REQUEST);
	            }
	        }
			else if(songCriteriaSearch!=null && songCriteriaSearch.getSortBy() != null )
	        	songList = songService.getSongList(songCriteriaSearch.getSortBy());
			
			if(songCriteriaSearch==null)
	        	songList = songService.getSongList();

			if(songList.isEmpty())
	            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			else {
				songResponseList = new ArrayList<>();
				for(Song item: songList) {
					int views = songService.getViewCount(item.getSongId());
					String songFile = backblazeService.getSongById(false, item.getSongId().toString());
					List<String> keywordNames = new ArrayList<>();
				    for (Keyword keyword : item.getKeywordlist()) {
				        keywordNames.add(keyword.getName());
				    }
					songResponseList.add(new SongResponse(
						    item.getSongId(),
						    item.getName(),
						    item.getDescription(),
						    item.getRank(),
						    item.getArtistName(),
						    item.getLyrics(),
						    views,
						    item.getAgeGroup().getName(),
						    keywordNames,
						    (item.getGenre() != null ? item.getGenre().getType() : "null"),
						    (item.getChart() != null ? item.getChart().getName() : "null"),
						    item.getImage(),
						    songFile
						));
				}
	            return new ResponseEntity<Object>( songResponseList , HttpStatus.OK);
			}
		}catch(Exception ex) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
		}
	}
	
	//INCREMENT VIEW_COUNT OF SONG BY ID
	//http://localhost:8080/yelody/song/incrementViewById?userId=&&songId=
	@CrossOrigin(origins = "*")
	@PostMapping("/incrementViewById")
	public ResponseEntity<Object> incrementViewById( @RequestParam(name="userId") @org.hibernate.validator.constraints.UUID UUID userId,
			@RequestParam(name="songId") @org.hibernate.validator.constraints.UUID UUID songId) {
        return songService.incrementViewCount(userId, songId);
	}
	
	//DELETE SONG
    //http://localhost:8080/yelody/song/deleteSong?id=
    @CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteSong")
    public ResponseEntity<?> deleteSong(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
    	songGet = null;
    	try {
    		songGet = songService.getSongById(id);
    		if(songGet!=null)
    			return songService.deleteSong(songGet.get());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SONG " + id + " NOT FOUND");
    	}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SONG " + id + " NOT FOUND");
    	}
    }
	
	//http://localhost:8080/yelody/song/tester
//	@CrossOrigin(origins = "*")
//	@PostMapping("/tester")
//	public String tester() throws IOException {
//        return ImageUtil.downloadFile(null,null);
//	}
}
