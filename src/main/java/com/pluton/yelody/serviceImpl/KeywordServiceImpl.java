package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.exceptions.ConstraintViolationHandler;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
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
	public Optional<Keyword> getKeywordById(UUID id) {
		return Optional.ofNullable(keywordRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("KEYWORD ID: " + id + " NOT FOUND")));
	}

	@Override
	public List<Keyword> getKeywordList() {
		return keywordRepository.findAll();
	}

	@Override
	public HttpStatus deleteKeyword(Keyword keyword) {
		if(keyword.getSongs()!=null && !keyword.getSongs().isEmpty())
			throw new ConstraintViolationHandler("Constraint violation: This KEYWORD is associated with SONG(s) and cannot be deleted.");

		keywordRepository.delete(keyword);
		return HttpStatus.OK;
	}

	@Override
	public Optional<Keyword> getKeywordByName(String name) {
		return Optional.ofNullable(keywordRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("KEYWORD NAME: " + name + " NOT FOUND")));
	}

}
