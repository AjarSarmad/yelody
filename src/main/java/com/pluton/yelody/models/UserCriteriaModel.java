package com.pluton.yelody.models;

import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCriteriaModel {
		
	private boolean doFilter;
	
	@Pattern(regexp = "^(user_name|email|phone|lastVisitDate|registrationDate)$", message = "Invalid sortBy value")
	private String sortBy;
	
	@Pattern(regexp = "^(user_name|email|phone|lastvisitdate|registrationdate)$", message = "Invalid filterBY value")
	private String filterBy;
	
	private String filter;
}
