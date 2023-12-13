package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.SongCriteriaSearch;
import com.pluton.yelody.DTOs.SongRequest;
import com.pluton.yelody.DTOs.SongtoChartRequest;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.User;

import jakarta.validation.Valid;

public interface SongService {
	public abstract List<Song> getSongList(Sort sort);
	
	public abstract List<Song> getSongList();
	
	public abstract Song postSong(Song song);

	public abstract List<Song> postSongs(List<Song> songList);
	
	public abstract GenericResponse incrementViewCount(UUID userId, UUID songId);
	
	public abstract Optional<Song> getSongByName(String name);
	
	public abstract int getViewCount(UUID songId);
	
	public abstract Optional<Song> getSongById(UUID id);

	public abstract GenericResponse deleteSong(Song song);
	
	public abstract List<Song> getRecommendedSongsForUser(User user);

	public abstract List<Song> getSongsBySpecifications(SongCriteriaSearch songCriteriaSearch);

	public abstract List<Song> getSongsBasedOnCriteria(SongCriteriaSearch songCriteriaSearch);

	public abstract List<Song> getSongById(List<UUID> songIds);

	public abstract GenericResponse postSongToChart(SongtoChartRequest songtoChartRequest);
	
	public abstract Optional<Song> getSongByRank(int rank);

	public abstract boolean validateSongRequest(@Valid SongRequest songRequest) throws Exception;
	
}
