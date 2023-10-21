package com.pluton.yelody.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongtoChartRequest {
	@NotNull
	private UUID chartId;
	
	@NotNull
	private String songName;
}
