package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.UserPrefrenceRequest;
import com.pluton.yelody.DTOs.UserPrefrenceResponse;
import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;

public interface UserPreferenceService {
	public abstract GenericResponse<UserPrefrenceResponse> createUserPreference(User user, UserPrefrenceRequest userPreferenceRequest);
	
	public abstract List<UserPreferences> getUserPreferencesByUserId(User user);
	
	public abstract Optional<UserPreferences> getUserPreferencesByID(UUID id);

	public abstract void deleteUserPreferences(UserPreferences userPreferences);
	
	public abstract GenericResponse<UserPrefrenceResponse> getUserPreferenceResponseByUserId(User user, String message);
	
	public abstract GenericResponse<UserPrefrenceResponse> updateUserPreferenceByUser(User user, UserPrefrenceRequest userPreferenceRequest);
}
