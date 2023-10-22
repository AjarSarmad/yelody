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
@Table(name="Age Group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgeGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="age_group_id" , nullable=false)
	private UUID ageGroupId;
	
	@Column(name="name" , nullable=false, unique=true)
	private String name;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ageGroup")
    @JsonIgnore
	private List<Song> songs = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ageGroup")
    @JsonIgnore
	private List<User> users = new ArrayList<>();
}