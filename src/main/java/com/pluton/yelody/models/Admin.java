package com.pluton.yelody.models;

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
@Table(name="Admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="admin_id" , nullable=false)
	private UUID adminId;
	
	@Column(name="user_name" , nullable=false)
	private String userName;
	
	@Column(name="password" , nullable=false)
	private String password;
	
	@Column(name="Email" , nullable=false)
	private String email;
	
	@Column(name="phone" , nullable=false)
	private String phone;
}