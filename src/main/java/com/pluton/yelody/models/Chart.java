package com.pluton.yelody.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Charts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="chart_id" , nullable=false)
	private UUID chartId;
	
	@Column(name="name" , nullable=false, unique=true)
	private String name;
	
	@Column(name="title" , nullable=false, unique=true)
	private String title;
	
	@Column(name="description" , nullable=false)
	private String description;
	
	@Column(name="new_flag" , nullable=false)
	private boolean newFlag;
	
	@Column(name="region" , nullable=false)
	private String region;
	
	@Column(name="`rank`" , nullable=false, unique=true)
	private int rank;

	@Column(name="view_count" , nullable=false)
	private int viewCount;

	@Lob
	@Column(name="cover_image" , nullable=true, columnDefinition="BLOB")
	private byte[] coverImage;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "chart")
	@JsonIgnoreProperties("chart")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Song> songs;

}