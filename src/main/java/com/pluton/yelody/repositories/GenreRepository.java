package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluton.yelody.models.Genre;

public interface GenreRepository extends JpaRepository<Genre , UUID>{
	boolean existsByType(String type);
	Optional<Genre> findByType(String type);
}
