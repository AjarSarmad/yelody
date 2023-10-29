package com.pluton.yelody.models;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name="playlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="user_playlist_id" , nullable=false)
	private UUID userPlaylistId;
	
	@JsonIgnoreProperties({"playlists"})
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	@JsonManagedReference
	@ManyToMany
    @JoinTable(name = "playlist_songs",
	        joinColumns = @JoinColumn(name = "playlist_id"),
	        inverseJoinColumns = @JoinColumn(name = "song_id"),
	        uniqueConstraints = @UniqueConstraint(columnNames = {"playlist_id", "song_id"}))
    private List<Song> songs;
    
    @Column(name="created_date" , nullable=false, unique=false)
	private Date createdDate;
    
    @Column(name="isFavourite" , nullable=false, unique=false)
	private boolean isFavourite;
}
