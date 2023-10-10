package com.pluton.yelody.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Songs")
@Data
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
	
	@Column(name="`rank`" , nullable=false)
	private int rank;
	
	@Column(name="artist_name" , nullable=false)
	private String artistName;

	@Column(name="lyrics" ,columnDefinition = "LONGTEXT", nullable=false)
	private String lyrics;
	
	

	@Column(name="view_count" , nullable=false)
	private int viewCount;

	@Lob
	@Column(name="song_image" , nullable=true, columnDefinition="BLOB")
	private byte[] songImage;
	
	
	@ManyToMany
    @JoinTable(
        name = "SongKeywords",
        joinColumns = @JoinColumn(name = "song_id"),
        inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    private List<Keyword> keywords = new ArrayList<>();
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre", nullable = false)
    private Genre genre;
	
	@ManyToMany
    @JoinTable(
        name = "SongChartMapping",
        joinColumns = @JoinColumn(name = "song_id"),
        inverseJoinColumns = @JoinColumn(name = "chart_id")
    )
    private List<Chart> charts = new ArrayList<>();
	
}