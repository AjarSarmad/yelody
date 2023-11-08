package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pluton.yelody.models.Playlist;

public interface PlaylistService {

	public abstract Playlist postPlaylist(Playlist playlistPost);
	
	public abstract List<Playlist> getPlaylistByUserId(UUID id);
	
	public abstract Optional<Playlist> getPlaylistById(UUID id);
	
	public abstract List<Playlist> getAllPlaylist();

	public abstract void deleteSongsFromPlayList(Playlist playlist, List<UUID> songIds);
	
	public abstract boolean isSongInPlaylist(Playlist playlist, UUID songId);

	public abstract void deletePlaylist(Playlist playlist);
	
	public abstract void addSongsToPlaylist(Playlist playlist, List<UUID> songIds);

	public abstract void addRemoveFavourite(Playlist playlist);

	public abstract List<Playlist> getFavouritePlaylistByUserId(UUID id);
}
