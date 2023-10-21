package com.pluton.yelody.models;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="user_id" , nullable=false)
	private UUID userId;
	
	@Column(name="user_name" , nullable=false, unique=true)
	private String userName;
	
	@Column(name="email" , nullable=false, unique=true)
	private String email;
	
	@Column(name="phone" , nullable=false)
	private String phone;
	
	@Column(name="last_visit_date" , nullable=false)
	private Date lastVisitDate;
	
	@Column(name="registration_date" , nullable=false)
	private Date registrationDate;
	
	@Column(name="yelo_points" , nullable=true)
	private double yeloPoints;
	
	@Column(name="sung_songs" , nullable=true)
	private int sungSongs;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_group", nullable = false)
    private AgeGroup ageGroup;
	

	@JsonBackReference
	@ManyToMany(mappedBy = "viewers")
    private List<Song> songViews;
}