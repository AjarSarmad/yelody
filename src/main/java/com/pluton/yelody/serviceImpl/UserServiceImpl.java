package com.pluton.yelody.serviceImpl;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pluton.yelody.models.User;
import com.pluton.yelody.repositories.UserRepository;
import com.pluton.yelody.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
	List<User> userList = null;
	Sort sort = null;
	Optional<User> user = null;
	
	@Override
	public List<User> getUserByEmail(String email, String sortBy){		
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
		
		return userRepository.findAll(filterByEmail(email),sort);			      
	}
	
	@Override
	public List<User> getUserByPhone(String phone, String sortBy){		
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
		
		return userRepository.findAll(filterByPhone(phone),sort);			      
	}
	
	@Override
	public List<User> getUserByLastVisitDate(String date, String sortBy){		
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = null;
		try {
			utilDate = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return userRepository.findAll(filterByLastVisitDate(new Date(utilDate.getTime())));			      
	}
	
	@Override
	public List<User> getUserByRegistrationDate(String date, String sortBy){		
		if(sortBy != null) {
			sort = Sort.by(Sort.Order.asc(sortBy));
		}else
			sort = Sort.unsorted();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = null;
		try {
			utilDate = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return userRepository.findAll(filterByRegistrationDate(new Date(utilDate.getTime())));			      
	}
	
	@Override
	public List<User> getUserList(String sortBy){		
		
		return userRepository.findAll(Sort.by(Sort.Order.asc(sortBy)));			      
	}
	
	@Override
	public Specification<User> filterByLastVisitDate(Date date){
		  return (root, query, criteriaBuilder)-> 
		      criteriaBuilder.equal(root.get("lastVisitDate"), date);
		}
	
	@Override
	public Specification<User> filterByRegistrationDate(Date date){
		  return (root, query, criteriaBuilder)-> 
		      criteriaBuilder.equal(root.get("registrationDate"), date);
		}
	
	@Override
	public Specification<User> filterByEmail(String email){
		  return (root, query, criteriaBuilder)-> 
		      criteriaBuilder.equal(root.get("email"), email);
		}
	
	@Override
	public Specification<User> filterByPhone(String phone){
		  return (root, query, criteriaBuilder)-> 
		      criteriaBuilder.equal(root.get("phone"), phone);
	}
	
	@Override
	public HttpStatus incrementSingCountById(UUID id) {
		if(userRepository.existsById(id)) {
		user = userRepository.findById(id);
		user.get().setSungSongs(user.get().getSungSongs()+1);
		userRepository.save(user.get());
		return HttpStatus.OK;
	}else
		return HttpStatus.NOT_FOUND;
}
}
