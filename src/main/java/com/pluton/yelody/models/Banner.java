package com.pluton.yelody.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="banner_id" , nullable=false)
	private UUID bannerId;
	
	@Column(name="location" , nullable=false)
	private String location;
	
	@Column(name="url" , nullable=false)
	private String url;
	
	@Column(name="language" , nullable=false)
	private String language;

	@Lob
	@Column(name="banner_image" , nullable=true, columnDefinition="BLOB")
	private byte[] bannerImage;
	
	
}