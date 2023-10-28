package com.pluton.yelody.DTOs;


import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgeGroupUpdateRequest {
	@NotNull(message = "ID MUST NOT BE NULL")
	private UUID id;
	
	@Pattern(regexp = "^[0-9]+-[0-9]+$", message = "Invalid age group format")
	@NotNull(message = "ageGroup MUST NOT BE NULL")
	private String ageGroup;
	
}
