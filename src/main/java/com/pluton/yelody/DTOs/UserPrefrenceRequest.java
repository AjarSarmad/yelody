package com.pluton.yelody.DTOs;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrefrenceRequest {
	private UUID userId;
	private List<UUID> ageGroup;
	private List<UUID> genre;
	private List<UUID> keyword;
}
