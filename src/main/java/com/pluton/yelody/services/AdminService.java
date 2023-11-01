package com.pluton.yelody.services;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.pluton.yelody.models.Admin;

public interface AdminService {
	public abstract Optional<Admin> findByEmail(String email);
	
	public abstract ResponseEntity<Object> postAdmin(Admin admin);
	
	public abstract void updateAdmin(Admin admin);
}
