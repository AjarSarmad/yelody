package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.exceptions.ConstraintViolationHandler;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.repositories.AgeGroupRepository;
import com.pluton.yelody.services.AgeGroupService;

@Service
public class AgeGroupServiceImpl implements AgeGroupService {
	@Autowired
	AgeGroupRepository ageGroupRepository;
	
	@Override
	public GenericResponse<AgeGroup> saveAgeGroup(AgeGroup ageGroup) {
		try {
			return GenericResponse.success(ageGroupRepository.save(ageGroup));
		}catch(Exception e) {
			return GenericResponse.error(e.getLocalizedMessage());
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
	public HttpStatus deleteAgeGroup(AgeGroup ageGroup) throws ConstraintViolationHandler {
		if(ageGroup.getSongs()!=null && !ageGroup.getSongs().isEmpty())
			throw new ConstraintViolationHandler("Constraint violation: This AGEGROUP is associated with SONG(s) and cannot be deleted.");
		if(ageGroup.getUsers()!=null && !ageGroup.getUsers().isEmpty())
			throw new ConstraintViolationHandler("Constraint violation: This AGEGROUP is associated with USER(s) and cannot be deleted.");

		ageGroupRepository.delete(ageGroup);
		return HttpStatus.OK;
	}

	@Override
	public Optional<AgeGroup> getAgeGroupByName(String name) {
		return Optional.ofNullable(ageGroupRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("AGEGROUP NAME: " + name + " NOT FOUND")));
	}

	@Override
	public boolean existsByName(String name) {
		return ageGroupRepository.existsByName(name);
	}

}
