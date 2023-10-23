package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.repositories.GenreRepository;
import com.pluton.yelody.services.GenreService;

@Service
public class GenreServiceImpl implements GenreService{
	
	@Autowired
	GenreRepository genreRepository;

	@Override
	public Optional<Genre> getGenreByID(UUID id) {
		return Optional.ofNullable(genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("GENRE ID: " + id + " NOT FOUND")));
	}

	@Override
	public List<Genre> getGenreList() {
		return genreRepository.findAll();
	}

	@Override
	public HttpStatus deleteGenre(Genre genre) {
		genreRepository.delete(genre);
		return HttpStatus.OK;
	}

	@Override
	public ResponseEntity<Object> createGenre(Genre genre) {
		try {
			return new ResponseEntity<Object>(genreRepository.save(genre),HttpStatus.CREATED);
		}catch(Exception  e) {
  			return new ResponseEntity<Object>(HttpStatus.FOUND);
		}
	}

	@Override
	public Optional<Genre> getGenreByType(String type) {
		return Optional.ofNullable(genreRepository.findByType(type).orElseThrow(() -> new EntityNotFoundException("GENRE TYPE: " + type + " NOT FOUND")));
	}
	
}
