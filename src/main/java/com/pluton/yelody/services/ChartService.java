package com.pluton.yelody.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.pluton.yelody.DTOs.ChartResponse;
import com.pluton.yelody.DTOs.GenericResponse;
import com.pluton.yelody.models.Chart;

public interface ChartService {
	public abstract GenericResponse<Chart> postChart(Chart chart);
	
	public abstract Optional<Chart> getChartById(UUID id);
	
	public abstract List<ChartResponse> getChartList();
	
	public abstract Optional<Chart> getChartByName(String name);

	public abstract HttpStatus deleteChart(Chart chart);
	
	public abstract Optional<Chart> getChartByRank(int rank); 
	
}
