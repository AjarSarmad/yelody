package com.pluton.yelody.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
	@Id
	@Column(name="Email" , nullable=false, unique=true)
	private String email;
	
	@Column(name="user_name" , nullable=false, unique=true)
	private String userName;
	
	@Column(name="password" , nullable=false)
	private String password;
	
	@Column(name="phone" , nullable=false)
	private String phone;
	
	@Column(name="otp" , nullable=true)
	private String otp;

	@Column(name="otp_requested_time" , nullable=true)
	private Date otpRequestedTime;
}