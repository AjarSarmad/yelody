package com.pluton.yelody.DTOs;

import java.util.ArrayList;
import java.util.List;
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
	private String ageGroup;
    private List<String> keywords = new ArrayList<>();
    private String genre;
    private String chart;
    private String imageFile;
    private String songFile;
}
