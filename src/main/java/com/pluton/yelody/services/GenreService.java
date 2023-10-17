package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pluton.yelody.models.Genre;

public interface GenreService {
	public abstract Optional<Genre> getGenreByID(UUID id);
	
	public abstract List<Genre> getGenreList();

	public abstract HttpStatus deleteGenre(Genre genre);

	public abstract ResponseEntity<Object> createGenre(Genre genre);

}
