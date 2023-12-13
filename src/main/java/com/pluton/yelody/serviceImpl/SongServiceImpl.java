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
import org.springframework.util.StringUtils;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.SongCriteriaSearch;
import com.pluton.yelody.DTOs.SongRequest;
import com.pluton.yelody.DTOs.SongtoChartRequest;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.Chart;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;
import com.pluton.yelody.repositories.SongRepository;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.BackblazeService;
import com.pluton.yelody.services.ChartService;
import com.pluton.yelody.services.SongService;
import com.pluton.yelody.services.UserPreferenceService;
import com.pluton.yelody.services.UserService;
import com.pluton.yelody.utilities.SongSpecifications;

import jakarta.validation.Valid;

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
	@Autowired
	ChartService chartService;
	
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
	public GenericResponse incrementViewCount(UUID userId, UUID songId) {
		try {
			if(songRepository.existsById(songId) && userRepository.existsById(userId)) {
				song = songRepository.findById(songId);
				song.get().getViewers().add(userRepository.findById(userId).get());
				songRepository.save(song.get());
				return GenericResponse.success("CREATED");
			}else
				return GenericResponse.error("NOT_FOUND");
		}catch(Exception ex) {
			return GenericResponse.error("VIEW ALREADY EXISTS");
		}
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
	public GenericResponse deleteSong(Song song) {
		try {
			songRepository.delete(song);
			backblazeService.deleteSongById(false, song.getSongId().toString());
//			ImageUtil.deleteFile(song.getImage());
			return GenericResponse.success("SONG " + song.getName() + " HAS BEEN DELETED SUCCESSFULLY");
		}catch(Exception ex){
			return GenericResponse.error(ex.getLocalizedMessage());
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

	@Override
	public List<Song> getSongsBasedOnCriteria(SongCriteriaSearch criteria) {
        if (criteria == null) {
            return getSongList();
        }

        if (criteria.hasFilters()) {
            return getSongsBySpecifications(criteria);
        } else if (criteria.getSortBy() != null && criteria.getOrder() != null) {
            return getSongList(SongSpecifications.getSortOrder(criteria.getSortBy(), criteria.getOrder()));
        } else {
            return getSongList();
        }
    }

	@Override
	public List<Song> getSongById(List<UUID> songIds) {
		songList = new ArrayList<>();
		for(UUID songId: songIds) {
			songList.add(getSongById(songId).get());
		}
		return songList;
	}

	@Override
	public List<Song> postSongs(List<Song> songList) {
		return songRepository.saveAll(songList);
	}

	@Override
	public GenericResponse postSongToChart(SongtoChartRequest songtoChartRequest) {
	    try {
	        Optional<Chart> chartOptional = chartService.getChartById(songtoChartRequest.getChartId());
	        List<Song> newSongList = getSongById(songtoChartRequest.getSongIds());

	        if(chartOptional.isPresent() && !newSongList.isEmpty()) {
	            Chart chart = chartOptional.get();
	            for (Song song : newSongList) {
	                Chart currentChart = song.getChart();
	                if (currentChart != null && !currentChart.equals(chart)) {
	                    return GenericResponse.error("Song with ID " + song.getSongId() + " is already in chart: " + currentChart.getChartId());
	                }
	            }

	            List<Song> currentlyAssignedSongs = songRepository.findByChart(chart);
	            currentlyAssignedSongs.forEach(song -> song.setChart(null));
	            songRepository.saveAll(currentlyAssignedSongs);

	            newSongList.forEach(song -> song.setChart(chart));
	            songRepository.saveAll(newSongList);

	            return GenericResponse.success("Chart updated with new songs.");
	        } else {
	        	return GenericResponse.error("Chart or Songs not found");
	        }
	    } catch(Exception ex) {
	    	return GenericResponse.error(ex.getLocalizedMessage());
	    }
	}

	@Override
	public Optional<Song> getSongByRank(int rank) {
		return songRepository.findByRank(rank);
	}

	@Override
	public boolean validateSongRequest(@Valid SongRequest songRequest) throws Exception {
		boolean isValid = true;
		if (!StringUtils.getFilenameExtension(songRequest.getFile().getOriginalFilename()).equalsIgnoreCase("mp3"))
            throw new Exception("SONG FILE FORMAT SHOULD BE .MP3");
        if (songRequest.getImage() == null || songRequest.getImage().isEmpty())
        	throw new Exception("IMAGE CANNOT BE NULL");
        if (songRequest.getLyrics_txt() == null || songRequest.getLyrics_txt().isEmpty())
        	throw new Exception("LYRICS .TXT CANNOT BE NULL");
        if (songRequest.getLyrics_xml() == null || songRequest.getLyrics_xml().isEmpty())
        	throw new Exception("LYRICS .XML CANNOT BE NULL");
        if(getSongByRank(songRequest.getRank()).isPresent())
        	throw new Exception("RANK ALREADY EXIST");
        
        return isValid;
	}


}
