package com.pluton.yelody.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.Playlist;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.repositories.PlaylistRepository;
import com.pluton.yelody.services.PlaylistService;
import com.pluton.yelody.services.SongService;

@Service
public class PlaylistServiceImpl implements PlaylistService{
	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	SongService songService;
	
	@Override
	public Playlist postPlaylist(Playlist playlistPost) {
		try {
			return playlistRepository.save(playlistPost);
		}
		catch(Exception e) {
			 if (e.getCause() instanceof ConstraintViolationException) {
		            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
		            String duplicateValue = constraintViolationException.getSQLException().getMessage();
		            throw new UniqueEntityException(duplicateValue);
		        } else {
		            throw new UniqueEntityException(e.getMessage());
		        }
		}
	}

	@Override
	public List<Playlist> getPlaylistByUserId(UUID id) {
		return playlistRepository.findByUserUserId(id);
	}

	@Override
	public Optional<Playlist> getPlaylistById(UUID id) {
		return Optional.ofNullable(playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("PLAYLIST ID: " + id + " NOT FOUND")));
	}

	@Override
	public List<Playlist> getAllPlaylist() {
		return playlistRepository.findAll();
	}

	@Override
	public void deleteSongsFromPlayList(Playlist playlist, List<UUID> songIds) {
		List<Song> songList = new ArrayList<>();
		
		for(UUID songId: songIds)
			songList.add(songService.getSongById(songId).get());
		
		for(Song song: songList) {
			if(!isSongInPlaylist(playlist, song.getSongId()))
				throw new EntityNotFoundException("SONG ID: " + song.getSongId() + " IS NOT FOUND IN PLAYLIST ID: " + playlist.getUserPlaylistId());
		}
		playlist.getSongs().removeAll(songList);
		playlistRepository.save(playlist);
	}

	@Override
	public boolean isSongInPlaylist(Playlist playlist, UUID songId) {
        for (Song song : playlist.getSongs()) {
            if (song.getSongId().equals(songId)) {
                return true;
            }
        }
	    return false;
	}

	@Override
	public void deletePlaylist(Playlist playlist) {
		playlistRepository.delete(playlist);
	}

	@Override
	public void addSongsToPlaylist(Playlist playlist, List<UUID> songIds) {
		List<Song> songList = new ArrayList<>();
			if(songIds!=null && !songIds.isEmpty()) {
	    		for(UUID songId: songIds) {
	    			songList.add(songService.getSongById(songId).get());
	    		}
	    		for(Song song: songList) {
	    			if(isSongInPlaylist(playlist, song.getSongId()))
	    				throw new EntityNotFoundException("SONG ID: " + song.getSongId() + " IS ALREADY IN PLAYLIST ID: " + playlist.getUserPlaylistId());
	    		}
	    		playlist.getSongs().addAll(songList);
	    		playlistRepository.save(playlist);
    		}
	}

	@Override
	public void addRemoveFavourite(Playlist playlist) {
		if(playlist.isFavourite())
			playlist.setFavourite(false);
		else
			playlist.setFavourite(true);
		playlistRepository.save(playlist);
	}

	@Override
	public List<Playlist> getFavouritePlaylistByUserId(UUID id) {
		 Specification<Playlist> userSpec = (root, query, builder) -> {
	            return builder.equal(root.get("user").get("userId"), id);
	        };
        Specification<Playlist> isFavorite = (root, query, builder) -> {
            return builder.isTrue(root.get("isFavourite"));
        };
        Specification<Playlist> combinedSpec = Specification.where(userSpec).and(isFavorite);

        return playlistRepository.findAll(combinedSpec); 
	}

}
