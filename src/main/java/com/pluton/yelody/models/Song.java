package com.pluton.yelody.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Songs",
uniqueConstraints = @UniqueConstraint(columnNames = { "lyrics" }))
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Song {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="song_id" , nullable=false)
	private UUID songId;
	
	@Column(name="name" , nullable=false)
	private String name;
	
	@Column(name="description" , nullable=false)
	private String description;
	
	@Column(name="`rank`" , nullable=false, unique=true)
	private int rank;
	
	@Column(name="artist_name" , nullable=false)
	private String artistName;

	@Column(name="lyrics" ,columnDefinition = "LONGTEXT", nullable=false)
	private String lyrics;
	
	@Column(name="image" , nullable=false)
	private String image;
	
	@JsonIgnore
	@JsonManagedReference
	@ManyToMany
    @JoinTable(
        name = "song_keyword",
        joinColumns = @JoinColumn(name = "song_id"),
        inverseJoinColumns = @JoinColumn(name = "keyword_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"song_id", "keyword_id"})
    )
    private List<Keyword> keywordlist;
	
	
	@JsonIgnore
	@JsonManagedReference
	@ManyToMany
    @JoinTable(
    name = "song_views",
    joinColumns = @JoinColumn(name = "song_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"song_id", "user_id"}))
	private List<User> viewers;
	
    @JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre", nullable = true)
    private Genre genre;
	
    @JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chart", nullable = true)
    private Chart chart;
    
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_group", nullable = false)
    private AgeGroup ageGroup;
	
	@JsonBackReference
	@ManyToMany(mappedBy = "sungSongs")
    private List<User> users;
	
}