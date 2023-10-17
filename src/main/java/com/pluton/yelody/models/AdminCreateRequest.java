package com.pluton.yelody.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateRequest {
	private String email;
	private String userName;
	private String password;
	private String phone;

}
