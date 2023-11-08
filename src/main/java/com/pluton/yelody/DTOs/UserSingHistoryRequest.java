package com.pluton.yelody.DTOs;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSingHistoryRequest {
	@NotNull(message = "USER ID MUST NOT BE NULL")
	private UUID userId;
	@NotNull(message = "SONG ID MUST NOT BE NULL")
	private UUID songId;
	@NotNull(message = ".MP3 FILE MUST NOT BE NULL")
	private MultipartFile file;
}
