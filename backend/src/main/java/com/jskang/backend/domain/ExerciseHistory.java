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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- 1. ID 자동 생성 어노테이션 추가
    private Long historyId; // 'historyId'가 'history_id'로 자동 매핑

    private Integer seq; // <-- 2. 'String'에서 'Integer'로 타입 수정 (DB의 INT와 일치)

    private LocalDateTime dt; // 'dt'가 'dt'로 자동 매핑 (OK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false) // DB의 'id'와 일치 (OK)
    @ToString.Exclude
    private UserM userM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false) // DB의 'sport_id'와 일치 (OK)
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