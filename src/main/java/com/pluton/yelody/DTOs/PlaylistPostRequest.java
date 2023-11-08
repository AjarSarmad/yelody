package com.pluton.yelody.DTOs;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistPostRequest {
	@NotNull(message = "USER ID MUST NOT BE NULL")
	private UUID userId;
	
	@NotNull(message = "SONG ID MUST NOT BE NULL")
	private List<UUID> songIds;
}
