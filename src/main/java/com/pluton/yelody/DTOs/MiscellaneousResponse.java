package com.pluton.yelody.DTOs;

import java.util.List;

import com.pluton.yelody.models.AgeGroup;
import com.pluton.yelody.models.Genre;
import com.pluton.yelody.models.Keyword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiscellaneousResponse {
	private List<AgeGroup> ageGroups;
	private List<Keyword> keywords;
	private List<Genre> genres;
}
