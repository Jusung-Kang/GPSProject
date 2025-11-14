package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "exercise_history")

public class ExerciseHistory {

    @Id
    @Column(name = "History_Id", nullable = false)
    private Long historyId;

    @Column(name = "Seq", nullable = false)
    private String seq;

    @Column(name = "Dt", nullable = false)
    private LocalDateTime dt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id", nullable = false)
    @ToString.Exclude
    private UserM userM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SportId", nullable = false)
    @ToString.Exclude
    private SportType sportType;

    @OneToMany(mappedBy = "exerciseHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<GpsData> gpsDataList = new ArrayList<>();

    public void addGpsData(GpsData gpsData){
        this.gpsDataList.add(gpsData);
        gpsData.setExerciseHistory(this);
    }
}
