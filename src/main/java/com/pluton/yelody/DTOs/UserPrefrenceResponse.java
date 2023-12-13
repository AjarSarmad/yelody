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
public class UserPrefrenceResponse {
	private List<AgeGroup> ageGroup;
	private List<Genre> genre;
	private List<Keyword> keyword;
}
