package com.pluton.yelody.models;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongRequest {
	@NotNull
	private String name;
	@NotNull
	private String description;
	@NotNull
	private int rank;
	@NotNull
	private String artistName;
	@NotNull
	private String lyrics;
	@NotNull
	private String genre;
}
