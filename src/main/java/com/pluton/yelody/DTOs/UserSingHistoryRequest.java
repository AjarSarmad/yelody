package com.pluton.yelody.DTOs;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSingHistoryRequest {
	private UUID userId;
	private UUID songId;
	private MultipartFile file;
}
