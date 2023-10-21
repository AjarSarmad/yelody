package com.pluton.yelody.DTOs;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongCriteriaSearch {
	
	private boolean doFilter;
	
	@Pattern(regexp = "^(name|artistname|rank|genre|keywords)$", message = "Invalid sortBy value")
	private String sortBy;
	
	@Pattern(regexp = "^(name|artistname|rank|genre|keywords)$", message = "Invalid filterBY value")
	private String filterBy;
	
	private String filter; 
}
