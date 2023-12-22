package com.pluton.yelody.services;

import org.springframework.web.multipart.MultipartFile;

public interface BackblazeService {
	    
    public abstract boolean uploadSong(boolean isUserRecordedSong, String songId, MultipartFile file);
    
    public abstract String getSongById(boolean isUserRecordedSong, String songId);
    
    public abstract boolean deleteSongById(boolean isUserRecordedSong, String songId);
}
