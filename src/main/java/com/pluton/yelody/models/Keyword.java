package com.pluton.yelody.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Keywords")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="keyword_id" , nullable=false)
	private UUID keywordId;
	
	@Column(name="name" , nullable=false, unique=true)
    private String name;
	
	@JsonBackReference
	@ManyToMany(mappedBy = "keywordlist")
    private List<Song> songs;
	
	@JsonIgnore
	@JsonManagedReference
    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPreferences> userPreferences = new ArrayList<>();

}	