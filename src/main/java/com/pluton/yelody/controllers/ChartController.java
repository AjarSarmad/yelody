package com.pluton.yelody.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.models.Chart;
import com.pluton.yelody.models.ChartRequest;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.SongtoChartRequest;
import com.pluton.yelody.services.ChartService;
import com.pluton.yelody.services.SongService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/chart")
public class ChartController {
	@Autowired
	ChartService chartService;
	@Autowired
	SongService songService;
	
	List<Chart> chartList = null;
	Chart chartPost = null;
	Optional<Chart> chartGet = null;
	Song songGet = null;
	List<Song> songList = null;
	
	//POST A BILLBOARD CHART
	//http://localhost:8080/yelody/chart/postChart
    @CrossOrigin(origins = "*")
  	@PostMapping("/postChart")
     public ResponseEntity<Object> postChart( @RequestBody @Valid ChartRequest chartRequest){
	    	chartPost = null;
	    	try {
	    		chartPost = new Chart(
	    				UUID.randomUUID(),
	    				chartRequest.getName(),
	    				chartRequest.getTitle(),
	    				chartRequest.getDescription(),
	    				chartRequest.isNewFlag(),
	    				chartRequest.getRegion(),
	    				chartRequest.getRank(),
	    				0,
	    				null,
	    				null
	    				);
	    		return chartService.postChart(chartPost);
	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	    	}
    }
    
    //POST SONG TO BILLBOARD CHART
    //http://localhost:8080/yelody/chart/postSongtoChart
    @CrossOrigin(origins = "*")
  	@PutMapping("/postSongtoChart")
     public ResponseEntity<Object> postSongtoChart( @RequestBody @Valid SongtoChartRequest songtoChartRequest){
	    	chartGet = null;
	    	songGet = null;
	    	songList = null;
	    	try {
	    		songGet = songService.getSongByName(songtoChartRequest.getSongName());
    			chartGet = chartService.getChartById(songtoChartRequest.getChartId());

	    		if(songGet!=null && chartGet!=null) {
//    				songList = chartGet.get().getSongs();
//    				songList.add(songGet);
//    				chartGet.get().setSongs(songList);
//	    			Chart chart = songGet.getChart();
//	    			if(chart==null)
	    			
    				songGet.setChart(chartGet.get());
    				return songService.postSong(songGet);
    			}
  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	    	}
    }
    

	//List of CHARTS
	//http://localhost:8080/yelody/chart/listCharts
	@CrossOrigin(origins = "*")
	@GetMapping("/listCharts")
	public ResponseEntity<Object> listCharts() {
		chartList = new ArrayList<>();
		try {
			chartList = chartService.getChartList();
			if(!(chartList.isEmpty()))
				return new ResponseEntity<Object>(chartList , HttpStatus.OK);
			
  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

		}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
	}
}
