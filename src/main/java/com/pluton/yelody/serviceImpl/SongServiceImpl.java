package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pluton.yelody.models.Song;
import com.pluton.yelody.repositories.SongRepository;
import com.pluton.yelody.services.SongService;

@Service
public class SongServiceImpl implements SongService{
	
	@Autowired
	SongRepository songRepository;
	
	List<Song> songList = null;
	Optional<Song> song = null;
	
	@Override
	public List<Song> getSongList(){
		return songRepository.findAll();
	}
	
	@Override
	public Song postSong(Song song) {
		return songRepository.save(song);
	}
	
	/** ADD UNIQUE USERS
	 * 
	 */
	@Override
	public HttpStatus incrementViewCount(UUID id) {
		if(songRepository.existsById(id)) {
			song = songRepository.findById(id);
			song.get().setViewCount(song.get().getViewCount()+1);
			songRepository.save(song.get());
			return HttpStatus.OK;
		}else
			return HttpStatus.NOT_FOUND;
	}
}
