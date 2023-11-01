package com.pluton.yelody.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="SongQueue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="queue_id" , nullable=false)
    private UUID songQueueId;
    
    @OneToOne(mappedBy="songQueue")
    private User user;
    
	@JsonManagedReference
    @OneToMany(mappedBy="songQueue", cascade = CascadeType.ALL)
    private List<SongQueueItem> items;
    
}