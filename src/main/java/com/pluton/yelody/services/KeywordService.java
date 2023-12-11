package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.models.Keyword;

public interface KeywordService {
	public abstract GenericResponse<Keyword> saveKeyword(Keyword keyword);
	
	public abstract Optional<Keyword> getKeywordById(UUID id);
	
	public abstract List<Keyword> getKeywordList();
	
	public abstract HttpStatus deleteKeyword(Keyword keyword);
	
	public abstract Optional<Keyword> getKeywordByName(String name);

}
