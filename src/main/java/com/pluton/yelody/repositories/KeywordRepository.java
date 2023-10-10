package com.pluton.yelody.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluton.yelody.models.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long>{
	

}
