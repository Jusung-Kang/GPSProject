package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
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

    private BigDecimal distance;

    private BigDecimal speed;

    private int gpsSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    @ToString.Exclude
    private ExerciseHistory exerciseHistory;

}