package com.pluton.yelody.DTOs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.services.AgeGroupService;
import com.pluton.yelody.services.GenreService;
import com.pluton.yelody.services.KeywordService;
import com.pluton.yelody.utilities.ImageUtil;

@Component
public class SongConverter {
    @Autowired
    private GenreService genreService;
    @Autowired
    private KeywordService keywordService;
    @Autowired
    private AgeGroupService ageGroupService;
    
	String imagePath = "ImageResources/SONG";

    public Song convertRequestToEntity(SongRequest songRequest) throws IOException{
    	 Optional<Genre> genre = genreService.getGenreByType(songRequest.getGenre());
    	    Optional<Keyword> keyword = keywordService.getKeywordByName(songRequest.getKeyword());
    	    Optional<AgeGroup> ageGroup = ageGroupService.getAgeGroupByName(songRequest.getAgeGroup());
    	    
    	    if (!genre.isPresent() || !keyword.isPresent() || !ageGroup.isPresent()) {
    	        return null;
    	    }

    	    UUID id = UUID.randomUUID();
    	    String imageResponse = ImageUtil.saveFile(imagePath, id.toString(), songRequest.getImage());

    	    Song song = new Song(
    	            id,
    	            songRequest.getSongName(),
    	            songRequest.getDescription(),
    	            songRequest.getRank(),
    	            songRequest.getArtistName(),
    	            songRequest.getLyrics(),
    	            imageResponse,
    	            Arrays.asList(keyword.get()),
    	            new ArrayList<>(),
    	            genre.get(),
    	            null,
    	            ageGroup.get(),
    	            new ArrayList<>(),
    	            new ArrayList<>()
    	    );

    	    return song;
    }
}
