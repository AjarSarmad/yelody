package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;
import com.pluton.yelody.repositories.UserPreferenceRepository;
import com.pluton.yelody.services.UserPreferenceService;
import com.pluton.yelody.services.UserService;

@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {
	@Autowired
	UserPreferenceRepository userPreferenceRepository;
	@Autowired
	UserService userService;
	
	@Override
	public GenericResponse<UserPreferences> createUserPreference(UserPreferences userPreferences) {
		try {
			return GenericResponse.success(userPreferenceRepository.save(userPreferences),"User Preference saved successfully.");
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
	public List<UserPreferences> getUserPreferencesByUserId(User user) {
		return userPreferenceRepository.getByUser(user);
	}

	@Override
	public Optional<UserPreferences> getUserPreferencesByID(UUID id) {
		return Optional.ofNullable(userPreferenceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("USER PREFERENCE ID: " + id + " NOT FOUND")));

	}

	@Override
	public void deleteUserPreferences(UserPreferences userPreferences) {
		userPreferenceRepository.delete(userPreferences);		
	}

}
