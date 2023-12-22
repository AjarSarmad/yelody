package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.exceptions.ConstraintViolationHandler;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
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
		if(genre.getSongs()!=null && !genre.getSongs().isEmpty())
			throw new ConstraintViolationHandler("Constraint violation: This GENRE is associated with SONG(s) and cannot be deleted.");

		genreRepository.delete(genre);
		return HttpStatus.OK;
	}

	@Override
	public GenericResponse<Genre> createGenre(Genre genre) {
		try {
			return GenericResponse.success(genreRepository.save(genre));
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
	public Optional<Genre> getGenreByType(String type) {
		return Optional.ofNullable(genreRepository.findByType(type).orElseThrow(() -> new EntityNotFoundException("GENRE TYPE: " + type + " NOT FOUND")));
	}
	
}
