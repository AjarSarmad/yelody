package com.pluton.yelody.models;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@Column(name="user_name" , nullable=false)
	private String userName;
	
	@Column(name="email" , nullable=false)
	private String email;
	
	@Column(name="phone" , nullable=false)
	private String phone;
	
	@Column(name="last_visit_date" , nullable=false)
	private Date lastVisitDate;
	
	@Column(name="registration_date" , nullable=false)
	private Date registrationDate;
	
	@Column(name="yelo_points" , nullable=false)
	private double yeloPoints;
	
	@Column(name="sung_songs" , nullable=false)
	private int sungSongs;

//	@Lob
//	@Column(name="Image" , nullable=true)
//	private byte[] image;
//	
	
}