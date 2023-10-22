package com.pluton.yelody.DTOs;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tester {
	private String path;
	private String fileName;
	private MultipartFile file;
}
