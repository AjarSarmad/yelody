package com.pluton.yelody.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.DTOs.ChartResponse;
import com.pluton.yelody.exceptions.ConstraintViolationHandler;
import com.pluton.yelody.exceptions.EntityNotFoundException;
import com.pluton.yelody.exceptions.UniqueEntityException;
import com.pluton.yelody.models.Chart;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.repositories.ChartRepository;
import com.pluton.yelody.services.ChartService;

@Service
public class ChartServiceImpl implements ChartService {
	@Autowired
	ChartRepository chartRepository;
	
	Optional<Chart> chartGet = null;
	List<Chart> chartList = null;
	
	@Override
	public ResponseEntity<Object> postChart(Chart chart){
		try {
			return new ResponseEntity<Object>(chartRepository.save(chart), HttpStatus.CREATED);
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
	public Optional<Chart> getChartById(UUID id) {
			return Optional.ofNullable(chartRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CHART ID: " + id + " NOT FOUND")));
	}

	@Override
	public List<ChartResponse> getChartList() {
		List<Chart> chartList = new ArrayList<>();
		List<ChartResponse> chartResponseList = new ArrayList<>();
		try {
			chartList = chartRepository.findAll();
			if(!chartList.isEmpty()) {
				for(Chart chart: chartList) {
					chartResponseList.add(chartResponseMapper(chart));
				}
			}
			return chartResponseList;
		}catch(Exception ex) {
			return null;
		}
	}

	@Override
	public Optional<Chart> getChartByName(String name) {
		return Optional.ofNullable(chartRepository.findChartByName(name).orElseThrow(() -> new EntityNotFoundException("CHART NAME: " + name + " NOT FOUND")));
	}

	@Override
	public HttpStatus deleteChart(Chart chart) {
		if(chart.getSongs()!=null && !chart.getSongs().isEmpty())
			throw new ConstraintViolationHandler("Constraint violation: This CHART is associated with SONG(s) and cannot be deleted.");

		chartRepository.delete(chart);
		return HttpStatus.OK;
	}
	
	public ChartResponse chartResponseMapper(Chart chart) {
		List<UUID> songIds = new ArrayList<>();
		try {
			ChartResponse chartResponse = new ChartResponse();
			chartResponse.setChartId(chart.getChartId());
			chartResponse.setDescription(chart.getDescription());
			chartResponse.setImage(chart.getImage());
			chartResponse.setName(chart.getName());
			chartResponse.setNewFlag(chart.isNewFlag());
			chartResponse.setRank(chart.getRank());
			chartResponse.setRegion(chart.getRegion());
			chartResponse.setTitle(chart.getTitle());
			chartResponse.setViewCount(chart.getViewCount());
			
			for(Song song: chart.getSongs()) {
				songIds.add(song.getSongId());
			}
			chartResponse.setSongIds(songIds);
			
			return chartResponse;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Optional<Chart> getChartByRank(int rank) {
		return chartRepository.findByRank(rank);
	}
	
}
