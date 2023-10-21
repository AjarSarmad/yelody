package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pluton.yelody.models.AgeGroup;

public interface AgeGroupService {
	public abstract ResponseEntity<Object> saveAgeGroup(AgeGroup ageGroup);
	
	public abstract Optional<AgeGroup> getAgeGroupById(UUID id);
	
	public abstract List<AgeGroup> getAgeGroupList();
	
	public abstract HttpStatus deleteAgeGroup(AgeGroup ageGroup);
	
	public abstract Optional<AgeGroup> getAgeGroupByName(String name);
	
	public boolean existsByName(String name);

}
