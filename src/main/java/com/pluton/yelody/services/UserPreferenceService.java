package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;

public interface UserPreferenceService {
	public abstract ResponseEntity<Object> createUserPreference(UserPreferences userPreferences);
	
	public abstract List<UserPreferences> getUserPreferencesByUserId(User user);
	
	public abstract Optional<UserPreferences> getUserPreferencesByID(UUID id);

	public abstract void deleteUserPreferences(UserPreferences userPreferences);
}
