package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.repositories.AgeGroupRepository;
import com.pluton.yelody.services.AgeGroupService;

@Service
public class AgeGroupServiceImpl implements AgeGroupService {
	@Autowired
	AgeGroupRepository ageGroupRepository;
	
	@Override
	public ResponseEntity<Object> saveAgeGroup(AgeGroup ageGroup) {
		try {
			return new ResponseEntity<Object>(ageGroupRepository.save(ageGroup),HttpStatus.CREATED);
		}catch(Exception e) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public Optional<AgeGroup> getAgeGroupById(UUID id) {
		return Optional.ofNullable(ageGroupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("AGEGROUP ID: " + id + " NOT FOUND")));

	}

	@Override
	public List<AgeGroup> getAgeGroupList() {
		return ageGroupRepository.findAll();

	}

	@Override
	public HttpStatus deleteAgeGroup(AgeGroup ageGroup) {
		ageGroupRepository.delete(ageGroup);
		return HttpStatus.OK;
	}

	@Override
	public Optional<AgeGroup> getAgeGroupByName(String name) {
		return Optional.ofNullable(ageGroupRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("AGEGROUP NAME: " + name + " NOT FOUND")));
	}

	@Override
	public boolean existsByName(String name) {
		if(ageGroupRepository.existsByName(name))
			return true;
		else
			return false;
	}

}
