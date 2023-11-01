package com.pluton.yelody.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name="SongQueueItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongQueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="queue_item_id" , nullable=false)
    private UUID songQueueItemId;

//    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "queue_id", nullable = false)
    private SongQueue songQueue;
    
    @Column(name="position" , nullable=false)
    private Integer position;
}