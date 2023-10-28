package com.pluton.yelody.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
	
	@Id
	@Column(name="banner_id" , nullable=false)
	private UUID bannerId;
	
	@Column(name="location" , nullable=false)
	private String location;
	
	@Column(name="language" , nullable=false)
	private String language;
	
	@Column(name="image" , nullable=false)
	private String image;
	
}