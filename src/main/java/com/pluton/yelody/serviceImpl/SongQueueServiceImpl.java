package com.pluton.yelody.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.SongQueue;
import com.pluton.yelody.models.SongQueueItem;
import com.pluton.yelody.repositories.SongQueueItemRepository;
import com.pluton.yelody.repositories.SongQueueRepository;
import com.pluton.yelody.services.SongQueueService;
import com.pluton.yelody.services.UserService;

@Service
public class SongQueueServiceImpl implements SongQueueService {

    @Autowired
    SongQueueRepository songQueueRepository;
    @Autowired
    SongQueueItemRepository songQueueItemRepository;
    @Autowired
    UserService userService;

    @Override
    public SongQueueItem addSongToQueue(UUID userId, Song song) {
        SongQueue userQueue = songQueueRepository.findByUserUserId(userId).orElse(null);
        
        if (userQueue == null) {
            userQueue = new SongQueue();
            userQueue.setUser(userService.getUserByID(userId).get());
            userQueue.setItems(new ArrayList<>());
            songQueueRepository.save(userQueue);
        }
        
        for (SongQueueItem existingItem : userQueue.getItems()) {
            if (existingItem.getSong().equals(song)) {
                return null; 
            }
        }
        
        SongQueueItem newItem = new SongQueueItem();
        newItem.setSong(song);
        newItem.setSongQueue(userQueue);
        newItem.setPosition(userQueue.getItems().size() + 1); 

        return songQueueItemRepository.save(newItem); 
    }

    @Override
    public boolean removeSongFromQueue(UUID userId, Song song) {
        SongQueue userQueue = getUserSongQueue(userId).get();
        SongQueueItem itemToRemove = userQueue.getItems().stream()
            .filter(item -> item.getSong().equals(song))
            .findFirst().orElse(null);

        if (itemToRemove != null) {
            userQueue.getItems().remove(itemToRemove);
            songQueueItemRepository.delete(itemToRemove);
            return true;
        }
        return false;
    }

    @Override
    public boolean reorderSongInQueue(UUID userId, Song reorderedSong, int desiredPosition) {
        SongQueue userQueue = getUserSongQueue(userId).get();
        List<SongQueueItem> queueItems = userQueue.getItems();

        Map<UUID, SongQueueItem> songToQueueItemMap = new HashMap<>();
        for (SongQueueItem queueItem : queueItems) {
            songToQueueItemMap.put(queueItem.getSong().getSongId(), queueItem);
        }

        SongQueueItem targetItem = songToQueueItemMap.get(reorderedSong.getSongId());

        int currentPosition = targetItem.getPosition();

        if (currentPosition < desiredPosition) {
            for (SongQueueItem queueItem : queueItems) {
                int position = queueItem.getPosition();
                if (position > currentPosition && position <= desiredPosition) {
                    queueItem.setPosition(position - 1);
                    songQueueItemRepository.save(queueItem);
                }
            }
        } else if (currentPosition > desiredPosition) {
            for (SongQueueItem queueItem : queueItems) {
                int position = queueItem.getPosition();
                if (position >= desiredPosition && position < currentPosition) {
                    queueItem.setPosition(position + 1);
                    songQueueItemRepository.save(queueItem);
                }
            }
        }

        targetItem.setPosition(desiredPosition);
        songQueueItemRepository.save(targetItem);

        return true; 
    }


    @Override
    public Optional<SongQueue> getUserSongQueue(UUID userId) {
		return Optional.ofNullable(songQueueRepository.findByUserUserId(userId).orElseThrow(() -> new EntityNotFoundException("SONG QUEUE WITH USER ID: " + userId + " NOT FOUND")));
    }
}
