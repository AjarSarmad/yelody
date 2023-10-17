package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Object> postSong(Song song) {
		try {
			return new ResponseEntity<Object>(songRepository.save(song),HttpStatus.CREATED);
		}catch(Exception  e) {
  			return new ResponseEntity<Object>(HttpStatus.FOUND);
		}
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
	
//	@Override
//	public Specification<Song> filterByKeyword(String keyword){
//		  return (root, query, criteriaBuilder)-> 
//		  {   root.join("s");
//		      criteriaBuilder.equal(root.get("keyword"), keyword);
//		  };
//		}
}
