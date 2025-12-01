package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "gps_data")

public class GpsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gpsId;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private BigDecimal altitude;

    private BigDecimal distance;

    private BigDecimal maxDistance;

    private BigDecimal speed;

    private BigDecimal maxSpeed;

    private Integer heartRate;

    private int gpsSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    @ToString.Exclude
    private ExerciseHistory exerciseHistory;

    public void setExerciseHistory(ExerciseHistory exerciseHistory) {
        this.exerciseHistory = exerciseHistory;
    }

    public void setMaxRecord(BigDecimal maxDistance,  BigDecimal maxSpeed) {
        this.maxDistance = maxDistance;
        this.maxSpeed = maxSpeed;
    }

}