package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.repositories.KeywordRepository;
import com.pluton.yelody.services.KeywordService;

@Service
public class KeywordServiceImpl implements KeywordService{
	@Autowired
	KeywordRepository keywordRepository;
	
	@Override
	public ResponseEntity<Object> saveKeyword(Keyword keyword) {
		try {
			return new ResponseEntity<Object>(keywordRepository.save(keyword),HttpStatus.CREATED);
		}catch(Exception e) {
  			return ResponseEntity.status(HttpStatus.FOUND).body("KEYWORD ALREADY EXISTS");
		}
	}

	@Override
	public Optional<Keyword> getKeywordById(UUID id) {
		return Optional.ofNullable(keywordRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("KEYWORD ID: " + id + " NOT FOUND")));
	}

	@Override
	public List<Keyword> getKeywordList() {
		return keywordRepository.findAll();
	}

	@Override
	public HttpStatus deleteKeyword(Keyword keyword) {
		keywordRepository.delete(keyword);
		return HttpStatus.OK;
	}

	@Override
	public Optional<Keyword> getKeywordByName(String name) {
		return Optional.ofNullable(keywordRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("KEYWORD NAME: " + name + " NOT FOUND")));
	}

}
