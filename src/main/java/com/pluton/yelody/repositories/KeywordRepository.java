package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, UUID>{
	Optional<Keyword> findByName(String name);
}
