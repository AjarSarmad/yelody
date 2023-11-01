package com.pluton.yelody.repositories;

import com.pluton.yelody.models.SongQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SongQueueItemRepository extends JpaRepository<SongQueueItem, UUID> {
}
