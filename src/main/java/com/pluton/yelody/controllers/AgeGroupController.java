package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.AgeGroupUpdateRequest;
import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.services.AgeGroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/ageGroup")
public class AgeGroupController {
	@Autowired
	AgeGroupService ageGroupService;
	
	Optional<AgeGroup> ageGroupGet = null;
	AgeGroup ageGroupPost = null;
	List<AgeGroup> ageGroupList = null;
	
	//Create a AGE GROUP
	//http://localhost:8080/yelody/ageGroup/addAgeGroup
	@CrossOrigin(origins = "*")
	@PostMapping("/addAgeGroup")
	public GenericResponse<AgeGroup> addAgeGroup(
			@RequestParam(name="ageGroup")
			final String ageGroup){
		ageGroupPost = null;
		if(!ageGroup.matches("^[0-9]+-[0-9]+$"))
			return GenericResponse.error("INVALID AGE GROUP PATTERN");
 
		if(!ageGroupService.existsByName(ageGroup)) {
		try {
				ageGroupPost = new AgeGroup(
	            UUID.randomUUID(),
	            ageGroup,
	            null,
	            null,
	            null
	        );
			
			return ageGroupService.saveAgeGroup(ageGroupPost);
		}catch(Exception ex) {
			return GenericResponse.error(ex.getLocalizedMessage());
		}}
		return null;
	}
	
	//GET AGE GROUP DETAILS BY ID
	//http://localhost:8080/yelody/ageGroup/getAgeGroupbyId
    @CrossOrigin(origins = "*")
  	@GetMapping("/getAgeGroupbyId")
    public GenericResponse<AgeGroup> getAgeGroupbyId(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
    	ageGroupGet = null;
    	try {
    		ageGroupGet = ageGroupService.getAgeGroupById(id);
    		if(ageGroupGet!=null)
    			return GenericResponse.success(ageGroupGet.get());
    		else
    			return GenericResponse.error("NOT FOUND");

    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
    
    //GET LIST OF AGE GROUPS
	//http://localhost:8080/yelody/ageGroup/getAgeGroupList
    @CrossOrigin(origins = "*")
  	@GetMapping("/getAgeGroupList")
    public GenericResponse<List<AgeGroup>> getAgeGroupList(){
    	ageGroupList = null;
    	try {
    		ageGroupList = new ArrayList<>(ageGroupService.getAgeGroupList());
    		if(ageGroupList!=null)
    			return GenericResponse.success(ageGroupList);
    		else
    			return GenericResponse.error("NOT FOUND.");

    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
    //UPDATE AGE GROUP DETAILS
	//http://localhost:8080/yelody/ageGroup/updateAgeGroup?
    @CrossOrigin(origins = "*")
  	@PutMapping("/updateAgeGroup")
    public GenericResponse<AgeGroup> updateAgeGroup(@RequestBody @Valid AgeGroupUpdateRequest ageGroupUpdateRequest){
    	ageGroupGet = null;
    	ageGroupPost = null;
    	try {
    		ageGroupGet = ageGroupService.getAgeGroupById(ageGroupUpdateRequest.getId());
    		if(ageGroupGet!=null) {
    			ageGroupPost = new AgeGroup(
    	  				ageGroupGet.get().getAgeGroupId(),
    	  				ageGroupUpdateRequest.getAgeGroup(),
    	  				ageGroupGet.get().getSongs(),
    	  				ageGroupGet.get().getUsers(),
    	  				ageGroupGet.get().getUserPreferences()
    					);
    			return ageGroupService.saveAgeGroup(ageGroupPost);
    			}
    		else
    			return GenericResponse.error("NOT FOUND.");

    	}catch(Exception ex) {
			ex.printStackTrace();
			return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
    //DELETE AGE GROUP DETAILS
	//http://localhost:8080/yelody/ageGroup/deleteAgeGroup?id=
    @CrossOrigin(origins = "*")
  	@DeleteMapping("/deleteAgeGroup")
    public GenericResponse deleteAgeGroup(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id){
    	ageGroupGet = null;
    	try {
    		ageGroupGet = ageGroupService.getAgeGroupById(id);
    		if(ageGroupGet!=null) {
    			ageGroupService.deleteAgeGroup(ageGroupGet.get());
    			return GenericResponse.success("Age Group deleted Successfully");
    		}
    		else
    			return GenericResponse.error("NOT FOUND.");
    	}catch(Exception ex) {
    		return GenericResponse.error(ex.getLocalizedMessage());
    	}
    }
    
}
	