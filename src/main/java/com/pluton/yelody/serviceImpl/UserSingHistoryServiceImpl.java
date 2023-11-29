package com.pluton.yelody.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserSingHistory;
import com.pluton.yelody.repositories.UserSingHistoryRepository;
import com.pluton.yelody.services.BackblazeService;
import com.pluton.yelody.services.UserSingHistoryService;
import com.pluton.yelody.utilities.ScoringAlgorithm;

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
		try {
			//NEED FIXING FOR TXT FILE
			long score = ScoringAlgorithm.compareLyrics(null, userSingHistory.getLyrics());
			userSingHistory.setScore(score);
			return userSingHistoryRepo.save(userSingHistory);
		}catch(Exception e) {
			 if (e.getCause() instanceof ConstraintViolationException) {
		            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
		            String duplicateValue = constraintViolationException.getSQLException().getMessage();
		            throw new UniqueEntityException(duplicateValue);
		        } else {
		            throw new UniqueEntityException(e.getMessage());
		        }
			}
	}

	@Override
	public Optional<UserSingHistory> getSingHistoryById(UUID id) {
		return Optional.ofNullable(userSingHistoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("SING HISTORY ID: " + id + " NOT FOUND")));
	}

	@Override
	public void deleteSingHistory(List<UUID> userSingHistoryIds) {
		singHistoryList = new ArrayList<>();
		for(UUID singHistoryId: userSingHistoryIds) {
			singHistoryList.add(getSingHistoryById(singHistoryId).get());
		}
		userSingHistoryRepo.deleteAll(singHistoryList);
	}

	@Override
	public List<UserSingHistory> getUserSingHistories(User user) {
		return userSingHistoryRepo.findByUser(user);
	}

}
