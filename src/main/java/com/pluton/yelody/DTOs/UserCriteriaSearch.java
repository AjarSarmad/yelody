package com.pluton.yelody.DTOs;

import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCriteriaSearch {
		
	private boolean doFilter;
	
	@Pattern(regexp = "^(userName|email|lastVisitDate|registrationDate)$", message = "Invalid sortBy value")
	private String sortBy;
	
	@Pattern(regexp = "^(userName|email|lastvisitdate|registrationdate)$", message = "Invalid filterBY value")
	private String filterBy;
	
	private String filter;
}
