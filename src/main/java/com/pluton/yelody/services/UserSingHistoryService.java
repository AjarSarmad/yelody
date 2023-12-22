package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserSingHistory;

public interface UserSingHistoryService {
	public abstract List<UserSingHistory> getSingHistoryList();
	
	public abstract UserSingHistory postSingHistory(UserSingHistory userSingHistory);
	
	public abstract Optional<UserSingHistory> getSingHistoryById(UUID id);

	public abstract void deleteSingHistory(List<UUID> userSingHistoryIds);
	
	public abstract List<UserSingHistory> getUserSingHistories(User user);
}
