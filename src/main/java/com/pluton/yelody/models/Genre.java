package com.pluton.yelody.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="genre_id" , nullable=false)
	private UUID genreId;
	
	@Column(name="type" , nullable=false, unique=true)
	private String type;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "genre")
//	@JsonIgnoreProperties("genre")
	@JsonIgnore
	private List<Song> songs = new ArrayList<>();
	
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPreferences> userPreferences = new ArrayList<>();

}