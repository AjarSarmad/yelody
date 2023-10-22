package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.UserCriteriaSearch;
import com.pluton.yelody.DTOs.UserRequest;
import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.User;
import com.pluton.yelody.services.AgeGroupService;
import com.pluton.yelody.services.UserService;
import com.pluton.yelody.utilities.ImageUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/user")
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	AgeGroupService ageGroupService;
	
	final String imagePath = "ImageResources/USER";
	List<User> userList = null;
	Optional<User> userGet = null;
	User userPost = null;
	Sort sort = null;
	
	//List of Users
	//http://localhost:8080/yelody/user/listUsers
	@CrossOrigin(origins = "*")
	@GetMapping("/listUsers")
	public ResponseEntity<Object> listUsers( @RequestBody(required = false) @Valid UserCriteriaSearch userCriteriaModel) {
	    try {
	        userList = new ArrayList<>(); 
	        
	        if (userCriteriaModel!=null && userCriteriaModel.getFilterBy() != null && userCriteriaModel.isDoFilter()) {
	            switch (userCriteriaModel.getFilterBy().toLowerCase()) {
	            	case "username":
	            		userList = userService.getUserByUserName(userCriteriaModel.getFilter(), userCriteriaModel.getSortBy() );
	            		break;
	                case "email":
	                    userList = userService.getUserByEmail(userCriteriaModel.getFilter(), userCriteriaModel.getSortBy() );
	                    break;
	                case "phone":
	                    userList = userService.getUserByPhone(userCriteriaModel.getFilter(), userCriteriaModel.getSortBy());
	                    break;
	                case "lastvisitdate":
	                    userList = userService.getUserByLastVisitDate(userCriteriaModel.getFilter(), userCriteriaModel.getSortBy());
	                    break;
	                case "registrationdate":
	                    userList = userService.getUserByRegistrationDate(userCriteriaModel.getFilter(), userCriteriaModel.getSortBy());
	                    break;
	                default:
	                    return new ResponseEntity<>("Invalid filterBy value", HttpStatus.BAD_REQUEST);
	            }
	        } else if(userCriteriaModel!=null && userCriteriaModel.getSortBy() != null)
	        	userList = userService.getUserList(userCriteriaModel.getSortBy());
	        
	        if (userCriteriaModel == null)
	        	userList = userService.getUserList();

	        if (userList.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        } else {
	            return new ResponseEntity<>(userList, HttpStatus.OK);
	        }
	    } catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
	    }
	}

	
	//GET USER DETAILS BY UUID
  	//http://localhost:8080/yelody/user/getUserDetails?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getUserDetails")
    public ResponseEntity<Object> getUserDetails(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID userId){
    	userGet = null;
    	try {
    		userGet = userService.getUserByID(userId);
    		if(userGet!=null || !(userGet.isEmpty()))
    			return new ResponseEntity<Object>(userGet, HttpStatus.FOUND);
    		else
      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
	
    //POST USER
  	//http://localhost:8080/yelody/user/postUser
    @CrossOrigin(origins = "*")
  	@PostMapping("/postUser")
    public ResponseEntity<Object> postUser(@ModelAttribute @Valid UserRequest userRequest){
    	userPost = null;
    	Optional<AgeGroup> ageGroup = null;
		String imageResponse = null;
    	try {
    		ageGroup = ageGroupService.getAgeGroupByName(userRequest.getAgeGroup());
    		
    		if(ageGroup!=null) {
    			
    			UUID id = UUID.randomUUID();
    			if(!userRequest.getImage().isEmpty())
    				imageResponse = ImageUtil.saveFile(imagePath, id.toString(), userRequest.getImage());
    			else
    				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IMAGE CANNOT BE NULL");
    			
	    		userPost = new User(
	    					id,
							userRequest.getUserName(),
							userRequest.getEmail(),
							userRequest.getPhone(),
							 new java.sql.Date(System.currentTimeMillis()),
							 new java.sql.Date(System.currentTimeMillis()),
							0,
							ageGroup.get(),
							new ArrayList<Song>(),
							new ArrayList<Song>(),
							imageResponse
	    				);
    		}
  			return userService.saveUser(userPost);
    	}catch(Exception ex) {
  			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AgeGroup not found");
    	}
    }
	
//    //INCREMENT VIEW_COUNT OF SONG BY ID
//  	//http://localhost:8080/yelody/user/incrementSingCountById?id=
//  	@CrossOrigin(origins = "*")
//  	@PostMapping("/incrementSingCountById")
//  	public ResponseEntity<Object> incrementSingCountById(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id) {
//          return new ResponseEntity<Object>(userService.incrementSingCountById(id));
//  	}
//	
}
