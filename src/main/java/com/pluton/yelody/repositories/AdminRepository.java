package com.pluton.yelody.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin , String>{
	Optional<Admin> findByEmail (String email);
}
