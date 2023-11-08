package com.pluton.yelody.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@Column(name="user_id" , nullable=false)
	private UUID userId;
	
	@Column(name="user_name" , nullable=false, unique=true)
	private String userName;
	
	@Column(name="email" , nullable=false, unique=true)
	private String email;
	
	@Column(name="password" , nullable=true)
	private String password;
	
	@Column(name="otp" , nullable=true)
	private String otp;

	@Column(name="otp_requested_time" , nullable=true)
	private Date otpRequestedTime;
	
	@Column(name="phone" , nullable=false)
	private String phone;
	
	@Column(name="last_visit_date" , nullable=false)
	private Date lastVisitDate;
	
	@Column(name="registration_date" , nullable=false)
	private Date registrationDate;
	
	@Column(name="yelo_points" , nullable=true)
	private double yeloPoints;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_group", nullable = false)
    private AgeGroup ageGroup;

	@JsonBackReference
	@ManyToMany(mappedBy = "viewers")
    private List<Song> songViews;
	
	@JsonIgnore
	@JsonManagedReference
	@ManyToMany
    @JoinTable(name = "user_sing_history",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "song_id"})
    )
    private List<Song> sungSongs;
	
	@Column(name="image" , nullable=false)
	private String image;
	
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Playlist> playlists;
	
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPreferences> preferences = new ArrayList<>();
	
	@JsonIgnore
	@JsonManagedReference
	@OneToOne(mappedBy="user")
    private SongQueue songQueue;

}