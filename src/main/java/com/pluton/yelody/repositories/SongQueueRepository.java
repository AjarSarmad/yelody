package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluton.yelody.models.SongQueue;

public interface SongQueueRepository extends JpaRepository<SongQueue, UUID> {
    Optional<SongQueue> findByUserUserId(UUID userId);
}
