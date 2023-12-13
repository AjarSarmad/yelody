package com.pluton.yelody.models;

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
@Table(name="UserPreferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {
	
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="preference_id", nullable = false)
    private UUID preferenceId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="genre_id", nullable=true)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name="keyword_id", nullable=true)
    private Keyword keyword;
    
    @ManyToOne
    @JoinColumn(name="age_group", nullable=true)
    private AgeGroup ageGroup;

}