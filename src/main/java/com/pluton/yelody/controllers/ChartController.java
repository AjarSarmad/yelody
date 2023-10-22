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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pluton.yelody.DTOs.ChartRequest;
import com.pluton.yelody.DTOs.SongtoChartRequest;
import com.pluton.yelody.models.Chart;
import com.pluton.yelody.models.Song;
import com.pluton.yelody.services.ChartService;
import com.pluton.yelody.services.SongService;
import com.pluton.yelody.utilities.ImageUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/yelody/chart")
public class ChartController {
	@Autowired
	ChartService chartService;
	@Autowired
	SongService songService;
	
	final String imagePath = "ImageResources/CHART";
	List<Chart> chartList = null;
	Chart chartPost = null;
	Optional<Chart> chartGet = null;
	Song songGet = null;
	List<Song> songList = null;
	
	//POST A BILLBOARD CHART
	//http://localhost:8080/yelody/chart/postChart
    @CrossOrigin(origins = "*")
  	@PostMapping("/postChart")
     public ResponseEntity<Object> postChart( @ModelAttribute @Valid ChartRequest chartRequest){
	    	chartPost = null;
			String imageResponse = null;
	    	try {
				UUID id = UUID.randomUUID();
				if(!chartRequest.getImage().isEmpty())
					imageResponse = ImageUtil.saveFile(imagePath, id.toString(), chartRequest.getImage());
				else
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IMAGE CANNOT BE NULL");
				
	    		chartPost = new Chart(
	    				id,
	    				chartRequest.getName(),
	    				chartRequest.getTitle(),
	    				chartRequest.getDescription(),
	    				chartRequest.isNewFlag(),
	    				chartRequest.getRegion(),
	    				chartRequest.getRank(),
	    				0,
	    				imageResponse,
	    				new ArrayList<Song>()
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
    				songGet.setChart(chartGet.get());
				
    			Song songPost = songService.postSong(songGet);
    			if(songPost!=null)
    	  			return new ResponseEntity<Object>(songPost, HttpStatus.CREATED);
    			}
  			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

	    	}catch(Exception ex) {
	  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	    	}
    }
    
    //UPDATE CHART DETAILS
   	//http://localhost:8080/yelody/chart/updateChart?id=
    @CrossOrigin(origins = "*")
   	@PutMapping("/updateChart")
     public ResponseEntity<Object> updateChart(@RequestParam(name="id") @org.hibernate.validator.constraints.UUID UUID id, @ModelAttribute @Valid ChartRequest chartRequest){
     	chartGet = null;
     	chartPost = null;
    	String imageResponse = null;
     	try {
     		chartGet = chartService.getChartById(id);
     		if(!chartRequest.getImage().isEmpty())
				imageResponse = ImageUtil.saveFile(imagePath, chartGet.get().getChartId().toString(), chartRequest.getImage());
     		
     		chartPost = new Chart(
     				chartGet.get().getChartId(),
     				chartRequest.getName(),
     				chartRequest.getTitle(),
     				chartRequest.getDescription(),
     				chartRequest.isNewFlag(),
     				chartRequest.getRegion(),
     				chartRequest.getRank(),
     				chartGet.get().getViewCount(),
     				chartRequest.getImage().isEmpty()?chartGet.get().getImage():imageResponse,
     				chartGet.get().getSongs()
 					);
     		if(chartGet!=null) {
     			return new ResponseEntity<Object>(chartService.postChart(chartPost), HttpStatus.OK);
     			}
     		else
     			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

     	}catch(Exception ex) {
 			ex.printStackTrace();
 			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
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
	
	//GET CHART DETAILS BY ID
  	//http://localhost:8080/yelody/chart/getChartById?id=
    @CrossOrigin(origins = "*")
  	@GetMapping("/getChartById")
    public ResponseEntity<Object> getChartById(@RequestParam(name="id") UUID id){
    	chartGet = null;
    	try {
    		chartGet = chartService.getChartById(id);
    		if(chartGet!=null)
    			return new ResponseEntity<Object>(chartGet.get(), HttpStatus.OK);
    		else
      			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

    	}catch(Exception ex) {
  			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    	}
    }
    
}
