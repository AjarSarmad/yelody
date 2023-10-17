package com.pluton.yelody.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {
	private UUID songId;
	private String name;
	private String description;
	private int rank;
	private String artistName;
	private String lyrics;
	private int viewCount;
//	private byte[] songImage;
//    private List<Keyword> keywords = new ArrayList<>();
    private String genre;
    private String chart;
}
