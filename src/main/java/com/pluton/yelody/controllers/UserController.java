package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserCriteriaModel;
import com.pluton.yelody.models.UserRequest;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.UserService;

@RestController
@RequestMapping("/yelody/user")
@Validated
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;
	
	List<User> userList = null;
	Optional<User> user = null;
	Sort sort = null;
	
	//List of Users
	//http://localhost:8080/yelody/user/listUsers
	@CrossOrigin(origins = "*")
	@GetMapping("/listUsers")
	public ResponseEntity<Object> listUsers(@Valid @RequestBody(required = false) UserCriteriaModel userCriteriaModel) {
	    try {
	        userList = new ArrayList<>(); 
	        
	        if (userCriteriaModel!=null && userCriteriaModel.getFilterBy() != null && userCriteriaModel.isDoFilter()) {
	            switch (userCriteriaModel.getFilterBy().toLowerCase()) {
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
	        }else
	        	userList = userService.getUserList(userCriteriaModel.getSortBy());

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
    public ResponseEntity<Object> getUserDetails(@RequestParam(name="id")UUID userId){
    	try {
    		user = userRepository.findById(userId);
    		if(user!=null || !(user.isEmpty()))
    			return new ResponseEntity<Object>(user, HttpStatus.FOUND);
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
    public ResponseEntity<Object> postUser(@RequestBody UserRequest userRequest){
    	try {
  			return new ResponseEntity<Object>(userRepository.save(
  					new User(		UUID.randomUUID(),
  									userRequest.getUserName(),
  									userRequest.getEmail(),
  									userRequest.getPhone(),
  									 new java.sql.Date(System.currentTimeMillis()),
  									 new java.sql.Date(System.currentTimeMillis()),
  									0,
  									0
  							)
  					),HttpStatus.OK);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
	
    //INCREMENT VIEW_COUNT OF SONG BY ID
  	//http://localhost:8080/yelody/user/incrementSingCountById?id=
  	@CrossOrigin(origins = "*")
  	@PostMapping("/incrementSingCountById")
  	public ResponseEntity<Object> incrementSingCountById(@RequestParam(name="id")UUID id) {
          return new ResponseEntity<Object>(userService.incrementSingCountById(id));
  	}
	
}
