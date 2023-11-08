package com.pluton.yelody.DTOs;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartResponse {
	private UUID chartId;
	private String name;
	private String title;
	private String description;
	private boolean newFlag;
	private String region;
	private int rank;
	private int viewCount;
	private String image;
	private List<UUID> songIds;
}
