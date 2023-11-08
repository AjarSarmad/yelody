package com.pluton.yelody.DTOs;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserSingHistoryRequest {
	@NotNull(message = "SING HISTORY IDs MUST NOT BE NULL")
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
			message = "NOT A VALID UUID"
			)
	private List<UUID> singHistoryIds;
}
