package com.pluton.yelody.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluton.yelody.models.Admin;

public interface AdminRepository extends JpaRepository<Admin , UUID>{

}
