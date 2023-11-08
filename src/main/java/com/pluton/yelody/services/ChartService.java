package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pluton.yelody.DTOs.ChartResponse;
import com.pluton.yelody.models.Chart;

public interface ChartService {
	public abstract ResponseEntity<Object> postChart(Chart chart);
	
	public abstract Optional<Chart> getChartById(UUID id);
	
	public abstract List<ChartResponse> getChartList();
	
	public abstract Optional<Chart> getChartByName(String name);

	public abstract HttpStatus deleteChart(Chart chart);
	
	public abstract Optional<Chart> getChartByRank(int rank); 
	
}
