package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.UserSingHistory;
import com.pluton.yelody.repositories.UserSingHistoryRepository;
import com.pluton.yelody.services.BackblazeService;
import com.pluton.yelody.services.UserSingHistoryService;

@Service
public class UserSingHistoryServiceImpl implements UserSingHistoryService{
	@Autowired
	UserSingHistoryRepository userSingHistoryRepo;
	@Autowired
	BackblazeService backblazeService;
	
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
		return Optional.ofNullable(userSingHistoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("SING HISTORY ID: " + id + " NOT FOUND")));
	}

	@Override
	public ResponseEntity<?> deleteSingHistory(UserSingHistory userSingHistory) {
		try {
			userSingHistoryRepo.delete(userSingHistory);
			backblazeService.deleteSongById(true, userSingHistory.getSingHistoryId().toString());
			return ResponseEntity.status(HttpStatus.OK).body("SING HISTORY ID: " + userSingHistory.getSingHistoryId() + " HAS BEEN DELETED SUCCESSFULLY");
		}catch(Exception ex){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
		}
	}

}
