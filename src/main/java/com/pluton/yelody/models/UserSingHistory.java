package com.pluton.yelody.models;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_sing_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSingHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="sing_history_id" , nullable=false)
	private UUID singHistoryId;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    
	@Column(name="recording_date" , nullable=false, unique=false)
	private Date recordingDate;
	
	@Column(name="score" , nullable=false, unique=false)
	private long score;
	
	
}
