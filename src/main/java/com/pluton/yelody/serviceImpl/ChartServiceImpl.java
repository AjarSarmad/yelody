package com.pluton.yelody.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pluton.yelody.models.Chart;
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
		}catch(Exception ex) {
			return new ResponseEntity<Object>(HttpStatus.FOUND);
		}
	}

	@Override
	public Optional<Chart> getChartById(UUID id) {
		chartGet = null;
		try {
			chartGet = chartRepository.findById(id);
			if(chartGet!=null)
				return chartGet;

			return null;
		}catch(Exception ex) {
			return null;
		}
	}

	@Override
	public List<Chart> getChartList() {
		try {
			return chartRepository.findAll();
		}catch(Exception ex) {
			return null;
		}
	}

	@Override
	public Optional<Chart> getChartByName(String name) {
		return chartRepository.findChartByName(name);
	}
	
	
}
