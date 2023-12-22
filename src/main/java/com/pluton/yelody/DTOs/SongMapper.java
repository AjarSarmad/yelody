package com.pluton.yelody.DTOs;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.services.BackblazeService;
import com.pluton.yelody.services.SongService;

@Component
public class SongMapper {

    @Autowired
    SongService songService;
	@Autowired
	BackblazeService backblazeService;
	String songFile = null;
	
    public SongResponse songToSongResponse(Song song, boolean getFile) {
        int views = songService.getViewCount(song.getSongId());
        if(getFile)
        	songFile = backblazeService.getSongById(false, song.getSongId().toString());

        List<String> keywordNames = song.getKeywordlist().stream()
            .map(Keyword::getName)
            .collect(Collectors.toList());

        return new SongResponse(
            song.getSongId(),
            song.getName(),
            song.getDescription(),
            song.getRank(),
            song.getArtistName(),
            song.getLyrics_txt(),
            song.getLyrics_xml(),
            views,
            song.getAgeGroup().getName(),
            keywordNames,
            (song.getGenre() != null ? song.getGenre().getType() : null),
            (song.getChart() != null ? song.getChart().getName() : null),
            song.getImage(),
            (getFile? songFile: null)
        );
    }
}
