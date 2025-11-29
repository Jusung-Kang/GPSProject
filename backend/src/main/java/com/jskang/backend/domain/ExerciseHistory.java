package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "exercise_history")
public class ExerciseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    private Integer seq;

    private LocalDateTime dt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserM userM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
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

    public void setUserM(UserM userM) {
        this.userM = userM;
    }

    public void changeSportType(SportType sportType) {
        this.sportType = sportType;
    }
}