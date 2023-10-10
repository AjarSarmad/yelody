package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluton.yelody.models.Song;

public interface SongRepository extends JpaRepository<Song, UUID>{
	Optional<Song> findByName (String name);

}
