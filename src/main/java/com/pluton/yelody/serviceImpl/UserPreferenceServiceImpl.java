package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.DTOs.UserPrefrenceRequest;
import com.pluton.yelody.DTOs.UserPrefrenceResponse;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;
import com.pluton.yelody.repositories.UserPreferenceRepository;
import com.pluton.yelody.services.AgeGroupService;
import com.pluton.yelody.services.GenreService;
import com.pluton.yelody.services.KeywordService;
import com.pluton.yelody.services.UserPreferenceService;
import com.pluton.yelody.services.UserService;

@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {
	@Autowired
	UserPreferenceRepository userPreferenceRepository;
	@Autowired
	UserService userService;
	@Autowired
	AgeGroupService ageGroupService;
	@Autowired
	GenreService genreService;
	@Autowired
	KeywordService keywordService;
	
	@Override
	public GenericResponse<UserPrefrenceResponse> createUserPreference(User user, UserPrefrenceRequest userPreferenceRequest) {
		try {
			if(userPreferenceRequest.getAgeGroup()!=null && !userPreferenceRequest.getAgeGroup().isEmpty()) {
				for(UUID item: userPreferenceRequest.getAgeGroup()) {
					AgeGroup ageGroup = ageGroupService.getAgeGroupById(item).get();
					userPreferenceRepository.save(new UserPreferences(null, user, null, null, ageGroup));
				}
			}
			if(userPreferenceRequest.getGenre()!=null && !userPreferenceRequest.getGenre().isEmpty()) {
				for(UUID item: userPreferenceRequest.getGenre()) {
					Genre genre = genreService.getGenreByID(item).get();
					userPreferenceRepository.save(new UserPreferences(null, user, genre, null, null));
				}
			}
			if(userPreferenceRequest.getKeyword()!=null && !userPreferenceRequest.getKeyword().isEmpty()) {
				for(UUID item: userPreferenceRequest.getKeyword()) {
					Keyword keyword = keywordService.getKeywordById(item).get();
					userPreferenceRepository.save(new UserPreferences(null, user, null, keyword, null));
				}
			}
			
			return getUserPreferenceResponseByUserId(user ,"User Preference saved successfully.");
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
	public GenericResponse<UserPrefrenceResponse> updateUserPreferenceByUser(User user,
			UserPrefrenceRequest userPreferenceRequest) {
		try {
			List<UserPreferences> userPrefList = getUserPreferencesByUserId(user);
			if(!userPrefList.isEmpty()) {
				for(UserPreferences item: userPrefList) {
					deleteUserPreferences(item);
				}
			}
			return createUserPreference(user, userPreferenceRequest);
		}catch(Exception ex) {
        	return GenericResponse.error(ex.getLocalizedMessage());
        }
	}
	
	@Override
	public List<UserPreferences> getUserPreferencesByUserId(User user) {
		return userPreferenceRepository.getByUser(user);
	}
	
	@Override
	public GenericResponse<UserPrefrenceResponse> getUserPreferenceResponseByUserId(User user, String message) {
		try {
			List<UserPreferences> preferences = getUserPreferencesByUserId(user);
	        
	        List<Genre> genreList = preferences.stream()
	            .filter(p -> p.getGenre() != null)
	            .map(UserPreferences::getGenre)
	            .collect(Collectors.toList());
	        
	        List<AgeGroup> ageGroupList = preferences.stream()
		            .filter(p -> p.getAgeGroup() != null)
		            .map(UserPreferences::getAgeGroup)
		            .collect(Collectors.toList());
	        
	        List<Keyword> keywordList = preferences.stream()
	            .filter(p -> p.getKeyword() != null)
	            .map(UserPreferences::getKeyword)
	            .collect(Collectors.toList());
	        
	        return GenericResponse.success(new UserPrefrenceResponse(ageGroupList, genreList, keywordList), message.isBlank()?" ": message);
		}catch(Exception ex) {
	        	return GenericResponse.error(ex.getLocalizedMessage());
        }
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
