package com.pluton.yelody.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.SongCriteriaSearch;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;
import com.pluton.yelody.repositories.SongRepository;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.BackblazeService;
import com.pluton.yelody.services.SongService;
import com.pluton.yelody.services.UserPreferenceService;
import com.pluton.yelody.services.UserService;
import com.pluton.yelody.utilities.SongSpecifications;

@Service
public class SongServiceImpl implements SongService{
	
	@Autowired
	SongRepository songRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BackblazeService backblazeService;
	@Autowired
	UserService userService;
	@Autowired
	UserPreferenceService userPreferenceService;
	
	List<Song> songList = null;
	Optional<Song> song = null;
	Sort sort = null;
	
	@Override
	public List<Song> getSongList(Sort sortBy){
		return songRepository.findAll(sortBy);			      
	}
	
	@Override
	public List<Song> getSongList(){
		return songRepository.findAll();			      
	}
	
	@Override
	public Song postSong(Song song){
		try {
			return songRepository.save(song);
			
		}catch(Exception e) {
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
	public ResponseEntity<Object> incrementViewCount(UUID userId, UUID songId) {
		try {
			if(songRepository.existsById(songId) && userRepository.existsById(userId)) {
				song = songRepository.findById(songId);
				song.get().getViewers().add(userRepository.findById(userId).get());
				songRepository.save(song.get());
				 ResponseEntity.status(HttpStatus.OK).body("CREATED");
			}else
				 ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT_FOUND");
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.FOUND).body("VIEW ALREADY EXISTS");
		}
		return null;
	}
	
	@Override
	public Optional<Song> getSongByName(String name) {
			return Optional.ofNullable(songRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("SONG NAME: " + name + " NOT FOUND")));
	}
	
	@Override
	public int getViewCount(UUID songId) {
		song = null;
		List<User> userList = new ArrayList<>();
		try {
			song = songRepository.findById(songId);
			if(song!=null) {
				userList = userRepository.findBySongViews(song.get());
				if(!(userList.isEmpty()))
					return userList.size();
			}
		}catch(Exception ex) {
			System.err.print(ex);
		}
		return 0;
	}

	@Override
	public Optional<Song> getSongById(UUID id) {
		return Optional.ofNullable(songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SONG ID: " + id + " NOT FOUND")));
	}

	@Override
	public ResponseEntity<?> deleteSong(Song song) {
		try {
			songRepository.delete(song);
			backblazeService.deleteSongById(false, song.getSongId().toString());
//			ImageUtil.deleteFile(song.getImage());
			return ResponseEntity.status(HttpStatus.OK).body("SONG " + song.getName() + " HAS BEEN DELETED SUCCESSFULLY");
		}catch(Exception ex){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
		}
	}
	
	@Override
	public List<Song> getRecommendedSongsForUser(User user) {
        List<UserPreferences> preferences = userPreferenceService.getUserPreferencesByUserId(user);
        
        List<Genre> genres = preferences.stream()
            .filter(p -> p.getGenre() != null)
            .map(UserPreferences::getGenre)
            .collect(Collectors.toList());

        List<Keyword> keywords = preferences.stream()
            .filter(p -> p.getKeyword() != null)
            .map(UserPreferences::getKeyword)
            .collect(Collectors.toList());

        Specification<Song> spec = Specification.where(null);

        for (Genre genre : genres) {
            spec = spec.or(SongSpecifications.hasGenre(genre));
        }

        for (Keyword keyword : keywords) {
            spec = spec.or(SongSpecifications.hasKeyword(keyword));
        }

        List<Song> songs = songRepository.findAll(spec);

        Collections.shuffle(songs);
        return songs.stream().limit(15).collect(Collectors.toList());
    }

	@Override
	public List<Song> getSongsBySpecifications(SongCriteriaSearch songCriteriaSearch) {
		Specification<Song> spec = Specification.where(null);

        if (songCriteriaSearch.getName() != null) {
            spec = spec.and(SongSpecifications.hasName(songCriteriaSearch.getName()));
        }

        if (songCriteriaSearch.getArtistName() != null) {
            spec = spec.and(SongSpecifications.hasArtistName(songCriteriaSearch.getArtistName()));
        }

        if (songCriteriaSearch.getRank() != null) {
            spec = spec.and(SongSpecifications.hasRank(songCriteriaSearch.getRank()));
        }
        
        if (songCriteriaSearch.getGenre() != null) {
            spec = spec.and(SongSpecifications.hasGenre(songCriteriaSearch.getGenre()));
        }
        
        if (songCriteriaSearch.getKeyword() != null) {
            spec = spec.and(SongSpecifications.hasKeyword(songCriteriaSearch.getKeyword()));
        }
        Sort sort = SongSpecifications.getSortOrder(songCriteriaSearch.getSortBy(), songCriteriaSearch.getOrder());

        return songRepository.findAll(spec, sort);
	}

}
