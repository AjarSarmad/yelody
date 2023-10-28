package com.pluton.yelody.serviceImpl;

import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.Admin;
import com.pluton.yelody.repositories.AdminRepository;
import com.pluton.yelody.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService{
	@Autowired
	AdminRepository adminRepository;
	
	@Override
	public Optional<Admin> findByEmail(String email) {
		return adminRepository.findByEmail(email);
	}

	@Override
	public ResponseEntity<Object> postAdmin(Admin admin) {
		try {
		return new ResponseEntity<Object>(adminRepository.save(admin),HttpStatus.CREATED);
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
	public void updateAdmin(Admin admin) {
		adminRepository.save(admin);
	}

}
