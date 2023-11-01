package com.pluton.yelody.services;

import java.util.Optional;
import java.util.UUID;

import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.SongQueue;
import com.pluton.yelody.models.SongQueueItem;

public interface SongQueueService {

    public abstract SongQueueItem addSongToQueue(UUID userId, Song song);

    public abstract boolean removeSongFromQueue(UUID userId, Song song);

    public abstract boolean reorderSongInQueue(UUID userId, Song reorderedSong, int desiredPosition);

    public abstract Optional<SongQueue> getUserSongQueue(UUID userId);

}
