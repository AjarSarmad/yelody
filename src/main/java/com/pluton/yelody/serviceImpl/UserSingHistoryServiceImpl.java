package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluton.yelody.models.UserSingHistory;
import com.pluton.yelody.repositories.UserSingHistoryRepository;
import com.pluton.yelody.services.UserSingHistoryService;

@Service
public class UserSingHistoryServiceImpl implements UserSingHistoryService{
	@Autowired
	UserSingHistoryRepository userSingHistoryRepo;
	
	List<UserSingHistory> singHistoryList = null;
	Optional<UserSingHistory> singHistoryGet = null;
	UserSingHistory singHistoryPost = null;

	@Override
	public List<UserSingHistory> getSingHistoryList() {
		return userSingHistoryRepo.findAll();
	}

	@Override
	public UserSingHistory postSingHistory(UserSingHistory userSingHistory) {
		singHistoryPost = null;
		try {
			return userSingHistoryRepo.save(userSingHistory);
		}catch(Exception ex) {
			return null;
		}
	}

	@Override
	public Optional<UserSingHistory> getSingHistoryById(UUID id) {
		return userSingHistoryRepo.findById(id);
	}

}
