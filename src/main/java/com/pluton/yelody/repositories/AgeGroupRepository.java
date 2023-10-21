package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.AgeGroup;

@Repository
public interface AgeGroupRepository extends JpaRepository<AgeGroup, UUID>{
	Optional<AgeGroup> findByName(String name);
	boolean existsByName(String name);
}
