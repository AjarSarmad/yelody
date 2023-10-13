package com.pluton.yelody.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.pluton.yelody.models.Song;

public interface SongService {
	public abstract List<Song> getSongList();
	public abstract Song postSong(Song song);
	public abstract HttpStatus incrementViewCount(UUID id);
}
