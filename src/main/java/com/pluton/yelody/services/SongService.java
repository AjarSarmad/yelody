package com.pluton.yelody.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pluton.yelody.models.Song;

public interface SongService {
	public abstract List<Song> getSongList(String sort);
	
	public abstract List<Song> getSongList();
	
	public abstract ResponseEntity<Object> postSong(Song song);
	
	public abstract HttpStatus incrementViewCount(UUID id);
	
	public abstract List<Song> getSongByName(String filter, String sortBy);
	
	public abstract Song getSongByName(String name);
	
	public abstract List<Song> getSongByArtistName(String filter, String sortBy);
	
	public abstract List<Song> getSongByRank(String filter, String sortBy);
	
	public abstract List<Song> getSongByGenre(String filter, String sortBy);
	
//	public abstract List<Song> getSongByKeyword(String filter, String sortBy);
	
	public abstract Specification<Song> filterByName(String name);
	
	public abstract Specification<Song> filterByArtistName(String artistName);
	
	public abstract Specification<Song> filterByRank(int rank);
	
	public abstract Specification<Song> filterByGenre(String genre);

//	public abstract Specification<Song> filterByKeyword(String keyword);

	
}
