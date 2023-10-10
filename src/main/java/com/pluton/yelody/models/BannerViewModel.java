package com.pluton.yelody.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerViewModel {
	private String location;
	private String url;
	private String language;
	
}