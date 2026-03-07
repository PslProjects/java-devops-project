package com.scada.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "results")
@Data
public class ResultRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String adminName;
    
    @Column(name = "session_id")
    private String sessionId;


    private String faultLine;        // ✅ NEW
    private Integer fcbFlipCount;    // ✅ NEW

    private Double resultPercentage;
    private Long timeTakenMs;

    private Long startTime;
    private Long endTime;

    @Column(columnDefinition = "TEXT")
    private String correctSequence;

    @Column(columnDefinition = "TEXT")
    private String userSequence;

   
    private String status;

    private Instant createdAt = Instant.now();
}
