package com.pluton.yelody.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartRequest {
	@NotNull
    @Size(min = 5, max = 20)
	private String name;
	
	@NotNull
    @Size(min = 5, max = 20)
	private String title;
	
	@NotNull
	private String description;
	
	@NotNull
	private boolean newFlag;
	
	@NotNull
	private String region;
	
	@NotNull
	@Min(1)
	@Max(10)
	private int rank;

}
