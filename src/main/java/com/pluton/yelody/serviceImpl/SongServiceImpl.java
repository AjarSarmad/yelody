package com.pluton.yelody.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.SongRequestExceptions.AgeGroupNotFoundException;
import com.pluton.yelody.exceptions.SongRequestExceptions.ChartNotFoundException;
import com.pluton.yelody.exceptions.SongRequestExceptions.GenreNotFoundException;
import com.pluton.yelody.exceptions.SongRequestExceptions.KeywordNotFoundException;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.models.Chart;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.User;
import com.pluton.yelody.repositories.SongRepository;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.SongService;

@Service
public class SongServiceImpl implements SongService{
	
	@Autowired
	SongRepository songRepository;
	@Autowired
	UserRepository userRepository;
	
	List<Song> songList = null;
	Optional<Song> song = null;
	Sort sort = null;
	
	@Override
	public List<Song> getSongList(String sortBy){
		return songRepository.findAll(Sort.by(Sort.Order.asc(sortBy)));			      
	}
	
	@Override
	public List<Song> getSongList(){
		return songRepository.findAll();			      
	}
	
	@Override
	public Song postSong(Song song) {
		try {
			return songRepository.save(song);
		}catch(Exception  e) {
  			return null;
		}
	}
	
	/** ADD UNIQUE USERS
	 * 
	 */
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
	public List<Song> getSongByName(String filter, String sortBy){
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
		
		return songRepository.findAll(filterByName(filter), sort);
	}
	
	@Override
	public Song getSongByName(String name){
		song = null;
		try {
			song = songRepository.findByName(name);
			if(song!=null)
				return song.get();
			return null;
		}catch(Exception ex) {
			return null;
		}
	}
	@Override
	public List<Song> getSongByArtistName(String filter, String sortBy){
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
		
		return songRepository.findAll(filterByArtistName(filter), sort);
	}
	
	@Override
	public List<Song> getSongByRank(String filter, String sortBy){
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
		try {
			int rankFilter = Integer.parseInt(filter);
			return songRepository.findAll(filterByRank(rankFilter), sort);
		
		}catch(Exception ex) {
			return null;
		}
	}
	@Override
	public List<Song> getSongByGenre(String filter, String sortBy){
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
		
		return songRepository.findAll(filterByGenre(filter), sort);
	}
	
	
	@Override
	public Specification<Song> filterByName(String name){
		  return (root, query, criteriaBuilder)-> 
		      criteriaBuilder.equal(root.get("name"), name);
		}
	
	@Override
	public Specification<Song> filterByArtistName(String artistName){
		  return (root, query, criteriaBuilder)-> 
		      criteriaBuilder.equal(root.get("artistName"), artistName);
		}
	
	@Override
	public Specification<Song> filterByRank(int rank){
		  return (root, query, criteriaBuilder)-> 
		      criteriaBuilder.equal(root.get("rank"), rank);
		}
	
	@Override
	public Specification<Song> filterByGenre(String genre){
		  return (root, query, criteriaBuilder)-> 
	        criteriaBuilder.equal(root.get("genre").get("type"), genre);
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
	public void validateExistence(Genre genre, Keyword keyword, AgeGroup ageGroup, Chart chart) throws RuntimeException{
		if (genre == null) {
	        throw new GenreNotFoundException("Given Genre does not exist");
	    }

	    if (keyword == null) {
	        throw new KeywordNotFoundException("Given Keyword does not exist");
	    }

	    if (chart == null) {
	        throw new ChartNotFoundException("Given Chart does not exist");
	    }

	    if (ageGroup == null) {
	        throw new AgeGroupNotFoundException("Given AgeGroup does not exist");
	    }
	}
	
//	@Override
//	public Specification<Song> filterByKeyword(String keyword){
//		  return (root, query, criteriaBuilder)-> 
//		  {   root.join("s");
//		      criteriaBuilder.equal(root.get("keyword"), keyword);
//		  };
//		}
}
