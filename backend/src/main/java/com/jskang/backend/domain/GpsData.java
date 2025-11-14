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
    @Column(name = "Gps_Id", nullable = false)
    private Long gpsId;

    @Column(name = "Longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "Latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "Distance", nullable = false, precision = 10, scale = 2)
    private BigDecimal distance;

    @Column(name = "Speed", nullable = false, precision = 5, scale = 2)
    private BigDecimal speed;

    @Column(name = "Gps_Seq", nullable = false)
    private int gpsSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "History_Id", nullable = false)
    @ToString.Exclude
    private ExerciseHistory exerciseHistory;

}