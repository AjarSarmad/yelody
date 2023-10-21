package com.pluton.yelody.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import com.pluton.yelody.models.User;

public interface UserService {
	
	public abstract List<User> getUserByUserName(String userName, String sortBy);
	
	public abstract List<User> getUserByEmail(String email, String sortBy);
	
	public abstract List<User> getUserByPhone(String phone, String sortBy);
	
	public abstract List<User> getUserByLastVisitDate(String date, String sortBy);
	
	public abstract List<User> getUserByRegistrationDate(String date, String sortBy);
	
	public abstract List<User> getUserList(String sortBy);
	
	public abstract List<User> getUserList();
	
	public abstract Optional<User> getUserByID(UUID id);
	
	public abstract Specification<User> filterByLastVisitDate(Date date);
	
	public abstract Specification<User> filterByRegistrationDate(Date date);
	
	public abstract Specification<User> filterByEmail(String email);

	public abstract Specification<User> filterByUserName(String userName);
	
	public abstract Specification<User> filterByPhone(String phone);
	
//	public abstract HttpStatus incrementSingCountById(UUID id);
	
	public abstract ResponseEntity<Object> saveUser(User  user);
	
}
