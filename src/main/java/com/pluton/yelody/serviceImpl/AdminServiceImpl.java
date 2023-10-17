package com.pluton.yelody.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
		}
	}

	@Override
	public void updateAdmin(Admin admin) {
		adminRepository.save(admin);
	}

}
