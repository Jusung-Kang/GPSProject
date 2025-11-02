package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SPORT_TYPE")

public class SportType {

    @Id
    @Column(name = "SportId", length = 8, nullable = false)
    private String sportId;

    @Column(name = "SportNm", length = 200, nullable = false)
    private String sportNm;

    @OneToMany(mappedBy = "SportType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<AvailableSports> availableSports = new HashSet<>();

    @OneToMany(mappedBy = "SportType")
    @Builder.Default
    @ToString.Exclude
    private Set<ExerciseHistory> exerciseHistory = new HashSet<>();

}
