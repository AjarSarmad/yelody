package com.pluton.yelody.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongRequest {
	
	@NotNull(message = "Song Name Name not be NULL")
	private String songName;
	
	@NotNull(message = "Description not be NULL")
	private String description;
	
	@Max(10)
	@Min(1)
	private int rank;
	
	@NotNull(message = "ArtistName not be NULL")
	private String artistName;
	
	@NotNull(message = "Lyrics not be NULL")
	private String lyrics;
	
	@NotNull(message = "Genre not be NULL")
	private String genre;
}
