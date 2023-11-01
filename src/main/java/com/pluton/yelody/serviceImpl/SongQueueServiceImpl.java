package com.pluton.yelody.serviceImpl;

import java.sql.Date;
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
    public SongQueueItem addSongToQueue(UUID userId, Song song) throws Exception {
        SongQueue userQueue = songQueueRepository.findByUserUserId(userId).orElse(null);
        
        if (userQueue == null) {
            userQueue = new SongQueue();
            userQueue.setUser(userService.getUserByID(userId).get());
            userQueue.setItems(new ArrayList<>());
            userQueue.setCreatedDate(new Date(System.currentTimeMillis()));
            songQueueRepository.save(userQueue);
        }
        
        for (SongQueueItem existingItem : userQueue.getItems()) {
            if (existingItem.getSong().equals(song)) {
                throw new Exception("SONG ALREADY EXISTED IN THE GIVEN USER QUEUE"); 
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
        Optional<SongQueue> optionalUserQueue = getUserSongQueue(userId);

        SongQueue userQueue = optionalUserQueue.get();
        List<SongQueueItem> queueItems = userQueue.getItems();

        if (desiredPosition < 1 || desiredPosition > queueItems.size()) {
            throw new IllegalArgumentException("Invalid desired position. It should be between 1 and " + queueItems.size());
        }
        Map<UUID, SongQueueItem> songToQueueItemMap = new HashMap<>();
        for (SongQueueItem queueItem : queueItems) {
            songToQueueItemMap.put(queueItem.getSong().getSongId(), queueItem);
        }
        SongQueueItem targetItem = songToQueueItemMap.get(reorderedSong.getSongId());

        if (targetItem == null) {
            throw new IllegalArgumentException("The provided song is not present in the user's song queue.");
        }
        int currentPosition = targetItem.getPosition();
        if (currentPosition == desiredPosition) {
            return true;
        }
        // Adjust positions of the other songs
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
