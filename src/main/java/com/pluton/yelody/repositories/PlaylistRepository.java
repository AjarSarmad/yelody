package com.pluton.yelody.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID>, JpaSpecificationExecutor<Playlist> {
	List<Playlist> findByUserUserId(UUID id);
}
